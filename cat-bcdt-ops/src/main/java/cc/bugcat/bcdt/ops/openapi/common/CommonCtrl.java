package cc.bugcat.bcdt.ops.openapi.common;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.cache.spi.InnerCache;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.acore.webmvc.utils.ServletUtil;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.service.inter.BcParamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Set;


/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "公共" )
@Controller
public class CommonCtrl {
    
    
    @Autowired
    private BcParamService paramService;
    
    
    @GetMapping({"/", "/web/"})
    @ACS(any = true)
    public RedirectView index(HttpServletRequest request) {
        
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/web/index.html");
        redirect.setHttp10Compatible(false);
        
        String url = request.getRequestURL().toString();
        String ipaddr = ServletUtil.getIpAddr(request);
        boolean isHttps = url.startsWith("https://");
        boolean isLAN = ObjectUtil.isLAN(ipaddr);
        if ( !isHttps && isLAN ) { //不是https请求，且是内网请求，使用http
            redirect.setHttp10Compatible(true);
        }
        
        return redirect;
    }
    
    
    @ResponseBody
    @GetMapping("/cache/refresh")
    @ACS(Opers.login)
    public synchronized ResultEntity<Void> refresh(@RequestParam(required = false, name = "tags") String tags) {
        InnerCache cache = InnerCache.getCache();
        cache.deleteByTags(tags);
        return ResultEntity.ok();
    }
    
    
    
    /**
     * 获取参数
     * */
    @ResponseBody
    @GetMapping("/common/param")
    @ACS(Opers.login)
    public ResultEntity<JsonTools> params(@RequestParam("code") String code){
        JsonTools vo = new JsonTools();
        Set<String> codes = ObjectUtil.splitSet(code, ",");
        for ( String name : codes ) {
            String value = paramService.getValue(name, null);
            vo.put(name, value);
        }
        return ResultEntity.ok(vo);
    }
    

}
