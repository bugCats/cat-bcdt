package cc.bugcat.bcdt.ops.openapi.user.login;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfoVo;
import cc.bugcat.bcdt.ops.openapi.user.login.vi.UserLoginVi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "用户" )
@RestController
public class UserLoginCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "登录")
    @PostMapping("/user/login")
    @ACS(any = true)
    public ResultEntity<UserInfoVo> openApi(@Validated @RequestBody UserLoginVi req) {
        return super.execute(UserLoginService.class, req);
    }
    
}
