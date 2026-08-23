package cc.bugcat.bcdt.ops.openapi.board.count.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class BoardCount {
    
    @Schema(description = "用户名")
    private String realname;
    
    @Schema(description = "数量")
    private Long count;
    
}
