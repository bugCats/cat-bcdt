package cc.bugcat.bcdt.ops.openapi.board.task;

import cc.bugcat.bcdt.ops.openapi.board.task.vi.BoardTaskVi;
import cc.bugcat.bcdt.ops.openapi.board.task.vo.BoardTaskVo;
import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class BoardTaskCtrl extends AbstractOpenApiCtrl {
	
	
	@Operation(summary = "用户、团队统计任务耗时", description = "统计用户、团队指定时间段内，任务-耗时")
	@PostMapping("/board/task")
	@ACS(Opers.login)
	public ResultEntity<BoardTaskVo> openApi(@RequestBody BoardTaskVi reqVi) {
		return super.execute(BoardTaskService.class, reqVi);
	}


}
