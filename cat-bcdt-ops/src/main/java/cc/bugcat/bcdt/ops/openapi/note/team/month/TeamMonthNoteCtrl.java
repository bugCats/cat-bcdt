package cc.bugcat.bcdt.ops.openapi.note.team.month;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.note.team.month.vo.TeamMonthNoteVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "日报" )
@RestController
public class TeamMonthNoteCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "根据月份，查询团队日报情况")
    @GetMapping("/team/month/note")
    @ACS(Opers.teamNote)
    public ResultEntity<TeamMonthNoteVo> openApi(@RequestParam(value = "date", required = false) String req) {
        return super.execute(TeamMonthNoteService.class, req);
    }
    
    
}
