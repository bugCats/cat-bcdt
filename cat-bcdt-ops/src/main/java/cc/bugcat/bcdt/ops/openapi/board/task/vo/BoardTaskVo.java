package cc.bugcat.bcdt.ops.openapi.board.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
@Schema
public class BoardTaskVo {
    
    
    @Schema(description = "统计开始时间：yyyy-MM-dd")
    private String start;
    
    @Schema(description = "统计结束时间：yyyy-MM-dd")
    private String end;
    
    @Schema(description = "数据")
    private List<BoardTask> list;
    
}
