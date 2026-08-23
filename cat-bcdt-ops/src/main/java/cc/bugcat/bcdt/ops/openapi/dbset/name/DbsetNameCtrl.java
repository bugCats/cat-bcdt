package cc.bugcat.bcdt.ops.openapi.dbset.name;

import cc.bugcat.bcdt.ops.constants.Opers;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import java.util.List;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.dbset.name.vi.DbsetNameVi;
import cc.bugcat.bcdt.ops.openapi.dbset.name.vo.DbsetNameVo;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "数据库配置" )
@RestController
public class DbsetNameCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "获取数据库名称")
	@PostMapping("/dbset/name")
	@ACS(Opers.login)
	public ResultEntity<List<DbsetNameVo>> openApi(@RequestBody DbsetNameVi req) {
		return super.execute(DbsetNameService.class, req);
	}


}
