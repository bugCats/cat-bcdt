package cc.bugcat.bcdt.ops.openapi.dbtask.edit;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.dbtask.edit.vi.DbtaskEditVi;
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
public class DbtaskEditCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "定时执行sql编辑")
	@PostMapping("/dbtask/edit")
	@ACS(Opers.dba)
	public ResultEntity<Void> openApi(@Validated @RequestBody DbtaskEditVi reqVi) {
		return super.execute(DbtaskEditService.class, reqVi);
	}


}
