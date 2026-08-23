package cc.bugcat.bcdt.ops.openapi.dbset.list;

import cc.bugcat.acore.framework.entity.PageInfo;
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
import cc.bugcat.bcdt.ops.openapi.dbset.list.vi.DbsetListVi;
import cc.bugcat.bcdt.ops.openapi.dbset.list.vo.DbsetListVo;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "数据库配置" )
@RestController
public class DbsetListCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "获取数据配置信息")
	@PostMapping("/dbset/list")
	@ACS(Opers.dba)
	public ResultEntity<PageInfo<DbsetListVo>> openApi(@Validated @RequestBody DbsetListVi req) {
		return super.execute(DbsetListService.class, req);
	}


}
