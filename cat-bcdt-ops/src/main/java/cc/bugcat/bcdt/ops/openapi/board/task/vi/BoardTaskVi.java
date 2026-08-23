package cc.bugcat.bcdt.ops.openapi.board.task.vi;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class BoardTaskVi {
    
    @Schema(description = "时间范围：week 本周，month 本月，year 本年，other 自定义")
    private String dateType;
    
    @Schema(description = "数据范围：1 个人；5 团队")
    private String dataRange;
    
    @Schema(description = "项目类型：10 task；20 bug")
    private String projectType;
    
    @Schema(description = "开始日期")
    private String start;
    
    @Schema(description = "结束日期")
    private String end;
    
}
