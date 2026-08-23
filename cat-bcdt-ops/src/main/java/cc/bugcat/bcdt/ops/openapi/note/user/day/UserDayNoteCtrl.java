package cc.bugcat.bcdt.ops.openapi.note.user.day;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.note.user.day.vo.UserDayNoteVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "日报" )
@RestController
public class UserDayNoteCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "根据日期，查询当前登录人的日报内容")
    @GetMapping("/user/day/note")
    @ACS(Opers.login)
    public ResultEntity<UserDayNoteVo> openApi(@RequestParam(value = "date", required = false) String req) {
        return super.execute(UserDayNoteService.class, req);
    }
    
    
}
