package cc.bugcat.bcdt.ops.openapi.board.count;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.board.count.vi.BoardCountVi;
import cc.bugcat.bcdt.ops.openapi.board.count.vo.BoardCountVo;
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
@Tag(name = "任务统计" )
@RestController
public class BoardCountCtrl extends AbstractOpenApiCtrl {



	@Operation(summary = "团队统计任务数量", description = "统计团队指定时间段内，用户-任务数量")
	@PostMapping("/board/team/count")
	@ACS(Opers.teamNote)
	public ResultEntity<BoardCountVo> openApi(@Validated @RequestBody BoardCountVi reqVi) {
		return super.execute(BoardCountService.class, reqVi);
	}


}
