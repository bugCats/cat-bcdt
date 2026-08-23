package cc.bugcat.bcdt.ops.openapi.user.sso;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.user.sso.vo.UserSsoTokenVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "用户" )
@RestController
public class UserSsoCtrl extends AbstractOpenApiCtrl {
    
    
    @Operation(summary = "获取sso token")
    @GetMapping("/user/sso/token")
    @ACS(Opers.login)
    public ResultEntity<UserSsoTokenVo> getSsoToken() {
        return super.execute(UserSsoTokenService.class, null);
    }
    
    
    
    @Operation(summary = "验证sso token")
    @GetMapping("/auth/user/sso")
    @ACS(any = true)
    public ResultEntity<String> validToken(@RequestParam("ssoToken") String req) {
        return super.execute(UserSsoValidService.class, req);
    }
    
}
