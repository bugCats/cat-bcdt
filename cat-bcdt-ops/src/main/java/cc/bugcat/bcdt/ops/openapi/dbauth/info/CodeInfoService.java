package cc.bugcat.bcdt.ops.openapi.dbauth.info;

import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.openapi.dbauth.info.vi.CodeInfoVo;
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
public class CodeInfoService extends AbstractOpenApiService<Void, CodeInfoVo> {
    
    @Autowired
    private IUserAuthCacheService userAuthCacheService;
    
    @Override
    public CodeInfoVo execute(Void reqVi, RequestContext context) {
        UserInfo user = UserUtil.getUser(context);
        CodeInfoVo vo = userAuthCacheService.myAuthInfo(user.getAccount());
        return vo;
    }
}
