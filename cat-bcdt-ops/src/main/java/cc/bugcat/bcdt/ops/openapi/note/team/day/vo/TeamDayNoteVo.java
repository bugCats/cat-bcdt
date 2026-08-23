package cc.bugcat.bcdt.ops.openapi.note.team.day.vo;

import cc.bugcat.bcdt.ops.openapi.note.user.day.vo.UserDayNote;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
@Schema
public class TeamDayNoteVo {

    @Schema(description = "账号")
    private String account;
    
    @Schema(description = "姓名")
    private String realname;
    
    @Schema(description = "日报内容")
    private List<UserDayNote> list;

}
