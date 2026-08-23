package cc.bugcat.bcdt.ops.openapi.dbProxy.info;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.dbProxy.info.vi.DbProxyInfoVi;
import cc.bugcat.bcdt.ops.openapi.dbProxy.info.vo.DbProxyInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "数据库代理配置" )
@RestController
public class DbProxyInfoCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "数据库代理详情")
	@GetMapping("/dbproxy/info")
	@ACS(Opers.dba)
	public ResultEntity<DbProxyInfoVo> openApi(@Validated @ModelAttribute DbProxyInfoVi req) {
		return super.execute(DbProxyInfoService.class, req);
	}


}
