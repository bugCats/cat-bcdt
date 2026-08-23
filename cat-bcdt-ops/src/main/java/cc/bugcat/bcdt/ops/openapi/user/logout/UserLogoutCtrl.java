package cc.bugcat.bcdt.ops.openapi.user.logout;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "用户" )
@RestController
public class UserLogoutCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "退出登录")
	@GetMapping("/user/logout")
	@ACS(any = true)
	public ResultEntity<Void> openApi() {
		return super.execute(UserLogoutService.class, null);
	}


}
