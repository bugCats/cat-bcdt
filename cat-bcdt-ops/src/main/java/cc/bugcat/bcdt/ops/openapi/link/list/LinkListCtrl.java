package cc.bugcat.bcdt.ops.openapi.link.list;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.link.list.vo.LinkColumn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "链接列表" )
@RestController
public class LinkListCtrl extends AbstractOpenApiCtrl {


	@Operation(summary = "链接列表", description = "链接列表")
	@GetMapping("/link/list")
	@ACS(Opers.login)
	public ResultEntity<List<LinkColumn>> openApi(@RequestParam("pageId") String reqVi) {
		return super.execute(LinkListService.class, reqVi);
	}


}
