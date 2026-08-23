package cc.bugcat.bcdt.ops.openapi.dbset.save;

import cc.bugcat.bcdt.ops.constants.Opers;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.dbset.save.vi.DbsetSaveVi;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "数据库配置" )
@RestController
public class DbsetSaveCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "新增、编辑数据库配置")
	@PostMapping("/dbset/save")
	@ACS(Opers.dba)
	public ResultEntity<Void> openApi(@RequestBody DbsetSaveVi req) {
		return super.execute(DbsetSaveService.class, req);
	}


}
