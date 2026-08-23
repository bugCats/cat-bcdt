package cc.bugcat.bcdt.ops.openapi.board.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema
public class BoardTask {
    
    @Schema(description = "任务id")
    private Long projectId;
    
    @Schema(description = "任务类型：10 task，20 bug")
    private String projectType;
    
    @Schema(description = "任务名称")
    private String projectName;
    
    @Schema(description = "任务耗时")
    private String costTime;
    
    @Schema(description = "参与人数")
    private String joins;
    
}
