package cc.bugcat.bcdt.ops.openapi.dbauth.valid;


import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.service.mysql.spi.IUserAuthCacheService;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class CodeValidService extends AbstractOpenApiService<String, Void> {
    
    @Autowired
    private IUserAuthCacheService userAuthCacheService;
    
    @Override
    public Void execute(String reqVi, RequestContext context) {
        UserInfo user = UserUtil.getUser(context);
        String authCode = user.getAccount() + "#" + reqVi;
        userAuthCacheService.codeValid(authCode, user.getIpAddr());
        return null;
    }
}
