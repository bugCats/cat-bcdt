package cc.bugcat.bcdt.ops.openapi.user.info;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfoVo;
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
public class UserInfoCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "获取登录信息", description = "获取登录信息")
	@GetMapping("/user/info")
	@ACS(Opers.login)
	public ResultEntity<UserInfoVo> openApi() {
		return super.execute(UserInfoService.class, null);
	}


}
