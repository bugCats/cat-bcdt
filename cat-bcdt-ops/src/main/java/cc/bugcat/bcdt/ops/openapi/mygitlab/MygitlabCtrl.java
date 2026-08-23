package cc.bugcat.bcdt.ops.openapi.mygitlab;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.cache.spi.InnerCache;
import cc.bugcat.acore.framework.constants.Const;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.config.SsoConfig;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


/**
 * 默认本地部署的gitlab服务端，实现单点登录到支持gitlab的第三方系统
 *
 * @author bugcat-cc
 */
@Tag(name = "gitlab 单点登录" )
@RestController
public class MygitlabCtrl {
    
    @Autowired
    private SsoConfig ssoConfig;
    
    @Autowired
    private InnerCache cache;
    
    /**
     * 办公平台单点登录跳转到集成gitlab的客户端
     * */
    @ACS(Opers.login)
    @GetMapping("/mygitlab/goto")
    public ResultEntity<String> ssoGoto(@RequestParam("clientId") String clientId, HttpServletRequest request){
        UserInfo user = UserUtil.getUserByToken(request.getHeader(Const.userToken));
        String code = ObjectUtil.uuid();

        cache.set("mygitlab:sso:code:" + code, InnerCache.min, user);
        
        String url = null;
        if ("SonarQubeApp".equals(clientId)){
            String sonarQube = ssoConfig.getSonarQube();
            url = sonarQube.replace("${code}", code);
        }
        
        return ResultEntity.ok(url);
        
    }
    
    
    
    /**
     * 客户端进入gitlab授权页面
     * response_type=code&client_id=SonarQubeApp&redirect_uri=http%3A%2F%2Flocalhost%3A11021%2Foauth2%2Fcallback%2Fgitlab&scope=read_user&state=rceqps4jus5hc1fff8vus95gsf
     * @return http://[客户端]/oauth2/callback/gitlab?code=授权码
     *  */
    @ACS(any = true)
    @RequestMapping(value = "/mygitlab/oauth/authorize", method = RequestMethod.GET)
    public String authorize(@RequestParam("redirect_uri") String redirectUri, @RequestParam("state") String state) {
        return "请从 办公平台 -> 代码检查 入口进入！";
    }
    
    
    
    /**
     * 客户端使用授权码换用户token
     * @param code 授权码
     * */
    @ACS(any = true)
    @RequestMapping(value = "/mygitlab/oauth/token", method = RequestMethod.POST)
    public String queryToken(@RequestParam("code") String code) {

        JsonTools resp = new JsonTools();

        UserInfo user = cache.get("mygitlab:sso:code:" + code, UserInfo.class);
        if ( user != null ) {
            cache.delete("mygitlab:sso:code:" + code); //删掉临时授权码
            
            String token = ObjectUtil.uuid();
            cache.set("mygitlab:sso:token:" + token, InnerCache.min, user);
            
            resp.put("access_token", token);
            resp.put("expires_in", 3600 * 24);
            resp.put("refresh_token", "accessToken");
            resp.put("token_type", "accessToken");
            resp.put("scope", "");
        }
        
        return resp.toJson();
    }
    
    
    
    @ACS(any = true)
    @RequestMapping(value = "/mygitlab/api/v4/user", method = RequestMethod.GET)
    public String queryUser(HttpServletRequest request) {

        JsonTools resp = new JsonTools(); // id,name,email,nickname
        
        String token = getToken(request);
        
        UserInfo user = cache.get("mygitlab:sso:token:" + token, UserInfo.class);
        if ( user != null ) {
            resp.put("id", user.getAccount().hashCode());
            resp.put("username", user.getAccount());
            resp.put("name", user.getRealname());
            resp.put("state", "active");
            resp.put("email", user.getAccount() + "@bugcat.cc");
        }
        
        return resp.toJson();
    }
    
    
    
    /**
     * 客户端验证gitlab服务地址
     * */
    @ACS(any = true)
    @RequestMapping(value = "/mygitlab/api/v4/projects", method = {RequestMethod.GET, RequestMethod.POST})
    public void queryProjects(HttpServletRequest request) {
        
    }
    
    
    
    /**
     * 客户端验证gitlab服务地址
     * */
    @ACS(any = true)
    @RequestMapping(value = "/mygitlab/api/v4/groups", method = RequestMethod.GET)
    public String queryGroup(HttpServletRequest request) {
        String token = getToken(request);
        
        if ( StringUtils.isBlank(token) ) {
            return "[]";
        }
        
        UserInfo user = cache.get("mygitlab:sso:token:" + token, UserInfo.class);
        if ( user == null ) {
            return "[]";
        }
        
        List<JsonTools> resp = new ArrayList<>();
        
        if ( Opers.hasOper(user, Opers.sonarAdmin) ) {
            JsonTools item = new JsonTools();
            item.put("id", 9);
            item.put("full_path", "sonar-admin");
            resp.add(item);
        } else 	{
            JsonTools item = new JsonTools();
            item.put("id", 1);
            item.put("full_path", "sonar-users");
            resp.add(item);
        }
        
        return JsonTools.toJson(resp);
    }
    
    
    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("authorization");
        String[] split = StringUtils.split(authorization, " ");
        return StringUtils.trim(split[1]);
    }
    
    
    @RequestMapping(value = "/mygitlab/*/**", method = {RequestMethod.GET, RequestMethod.POST})
    public void sso22(HttpServletRequest request) {
        try {
            Enumeration<String> headerNames = request.getHeaderNames();
            while ( headerNames.hasMoreElements() ) {
                String headerName = headerNames.nextElement();
                System.out.println(headerName + ":" + request.getHeader(headerName));
            }
            System.out.println(request.getRequestURI() + "  " + request.getMethod());
            System.out.println(IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8));
            System.out.println(JsonTools.toJson(request.getParameterMap()));
        } catch ( IOException ex ) {
            ex.printStackTrace();
		}
	}
	
    
    
}
