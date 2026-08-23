package cc.bugcat.bcdt.ops.openapi.dbtask.list;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.dbtask.list.vi.DbtaskListVi;
import cc.bugcat.bcdt.ops.openapi.dbtask.list.vo.DbtaskListVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "数据库版本" )
@RestController
public class DbtaskListCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "定时执行sql列表")
	@PostMapping("/dbtask/list")
	@ACS(Opers.dba)
	public ResultEntity<List<DbtaskListVo>> openApi(@Validated @RequestBody DbtaskListVi reqVi) {
		return super.execute(DbtaskListService.class, reqVi);
	}


}
