package cc.bugcat.bcdt.ops.openapi.note.team.month.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema
public class TeamMonthNoteVo {
    
    private String month;
    
    private List<String> dates;
    
    private List<UserDay> userDays;

}
