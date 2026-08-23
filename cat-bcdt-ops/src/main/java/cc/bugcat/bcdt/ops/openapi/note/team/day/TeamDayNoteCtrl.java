package cc.bugcat.bcdt.ops.openapi.note.team.day;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.note.team.day.vo.TeamDayNoteVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "日报" )
@RestController
public class TeamDayNoteCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "根据日期，查询所有人的日报")
    @GetMapping("/team/day/note")
    @ACS(Opers.teamNote)
    public ResultEntity<List<TeamDayNoteVo>> openApi(@RequestParam(value = "date", required = false) String req) {
        return super.execute(TeamDayNoteService.class, req);
    }
    
    
}
