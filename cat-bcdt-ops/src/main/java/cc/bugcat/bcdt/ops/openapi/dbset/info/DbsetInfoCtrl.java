package cc.bugcat.bcdt.ops.openapi.dbset.info;

import cc.bugcat.bcdt.ops.constants.Opers;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.dbset.info.vi.DbsetInfoVi;
import cc.bugcat.bcdt.ops.openapi.dbset.info.vo.DbsetInfoVo;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "数据库配置" )
@RestController
public class DbsetInfoCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "获取数据配置明细")
	@GetMapping("/dbset/info")
	@ACS(Opers.dba)
	public ResultEntity<DbsetInfoVo> openApi(@Validated @ModelAttribute DbsetInfoVi req) {
		return super.execute(DbsetInfoService.class, req);
	}


}
