package cc.bugcat.bcdt.ops.openapi.dbProxy.status;

import cc.bugcat.bcdt.ops.constants.Opers;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.dbProxy.status.vi.DbProxyStatusVi;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "数据库代理配置" )
@RestController
public class DbProxyStatusCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "数据库代理启用、禁用")
	@PostMapping("/dbproxy/status")
	@ACS(Opers.dba)
	public ResultEntity<Void> openApi(@Validated @RequestBody DbProxyStatusVi req) {
		return super.execute(DbProxyStatusService.class, req);
	}

	
}
