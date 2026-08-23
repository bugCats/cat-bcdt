package cc.bugcat.bcdt.ops.openapi.user.sso;

import cc.bugcat.acore.framework.cache.spi.InnerCache;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.MdEncrypt;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.acore.webmvc.utils.ServletUtil;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.openapi.user.sso.vo.UserSsoTokenVo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author bugcat-cc
 */
@Log4j2
@Service
public class UserSsoTokenService extends AbstractOpenApiService<Void, UserSsoTokenVo> {
    
    @Autowired
    private InnerCache cache;
    
    @Override
    public UserSsoTokenVo execute(Void reqVi, RequestContext context) {
        UserInfo user = UserUtil.getUser(context);
        
        String ssoToken = MdEncrypt.md5(user.getToken() + ObjectUtil.uuid());
        cache.set("sso:" + ssoToken, InnerCache.min, user);
        
        UserSsoTokenVo resp = new UserSsoTokenVo();
        resp.setToken(ssoToken);
        resp.setAccount(user.getAccount());
        
        String userIp = ServletUtil.getIpAddr(context.getRequest());
        log.warn(user.getRealname() + "[" + userIp + "]登录日志...");
        
        return resp;
        
    }
}
