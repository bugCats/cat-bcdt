package cc.bugcat.bcdt.ops.openapi.dbProxy.list;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.framework.entity.PageInfo;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.dbProxy.list.vi.DbProxyListVi;
import cc.bugcat.bcdt.ops.openapi.dbProxy.list.vo.DbProxyListVo;
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
@Tag(name = "数据库代理配置" )
@RestController
public class DbProxyListCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "获取数据配置信息")
	@PostMapping("/dbproxy/list")
	@ACS(Opers.dba)
	public ResultEntity<PageInfo<DbProxyListVo>> openApi(@Validated @RequestBody DbProxyListVi req) {
		return super.execute(DbProxyListService.class, req);
	}


}
