package cc.bugcat.bcdt.ops.openapi.dbProxy.save;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.dbProxy.save.vi.DbProxySaveVi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "数据库代理配置" )
@RestController
public class DbProxySaveCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "新增、编辑数据库配置")
	@PostMapping("/dbproxy/save")
	@ACS(Opers.dba)
	public ResultEntity<Void> openApi(@RequestBody DbProxySaveVi req) {
		return super.execute(DbProxySaveService.class, req);
	}


}
