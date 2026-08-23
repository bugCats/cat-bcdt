package cc.bugcat.bcdt.ops.openapi.dbtask.save;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.dbtask.save.vi.DbtaskSaveVi;
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
@Tag(name = "数据库版本" )
@RestController
public class DbtaskSaveCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "新增定时任务")
	@PostMapping("/dbtask/save")
	@ACS(Opers.dba)
	public ResultEntity<Void> openApi(@Validated @RequestBody DbtaskSaveVi reqVi) {
		return super.execute(DbtaskSaveService.class, reqVi);
	}


}
