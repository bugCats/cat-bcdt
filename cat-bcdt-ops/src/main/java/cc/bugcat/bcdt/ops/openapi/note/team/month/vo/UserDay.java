package cc.bugcat.bcdt.ops.openapi.note.team.month.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
@Schema
public class UserDay {
    
    private String account;
    
    private String realname;
    
    private List<Boolean> dates;
    
}
