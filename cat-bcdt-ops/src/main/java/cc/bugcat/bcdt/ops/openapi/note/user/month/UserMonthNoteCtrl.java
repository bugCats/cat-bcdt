package cc.bugcat.bcdt.ops.openapi.note.user.month;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "日报" )
@RestController
public class UserMonthNoteCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "根据月份，查询当前登录人日报情况")
    @GetMapping("/user/month/note")
    @ACS(Opers.login)
    public ResultEntity<List<String>> openApi(@RequestParam(value = "date", required = false) String req) {
        return super.execute(UserMonthNoteService.class, req);
    }
    
    
}
