package cc.bugcat.bcdt.ops.openapi.user.sso;

import cc.bugcat.acore.framework.cache.spi.InnerCache;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class UserSsoValidService extends AbstractOpenApiService<String, String> {
    
    @Autowired
    private InnerCache cache;
    
    @Override
    public String execute(String reqVi, RequestContext context) {
        try {
            UserInfo userInfo = cache.getAndRefresh("sso:" + reqVi, InnerCache.min, UserInfo.class);
            return userInfo != null ? userInfo.getAccount() : null;
        } catch ( Exception ex ) {
            return null;
        }
    }
}
