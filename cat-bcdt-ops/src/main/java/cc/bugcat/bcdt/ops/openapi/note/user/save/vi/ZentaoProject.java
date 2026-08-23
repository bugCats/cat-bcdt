package cc.bugcat.bcdt.ops.openapi.note.user.save.vi;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;


@Data
@Schema
public class ZentaoProject {

    @Schema(description = "汇报id")
    private Long id;
    
    @NotNull(message = "项目、任务id不能为空")
    private Long projectId;	// 项目、任务id

    @NotNull(message = "项目、任务类不能为空")
    private String projectType;	// 10 zt_task；20 zt_bug

    @NotBlank(message = "项目名称不能为空")
    private String projectName;	// 项目名称

    @NotBlank(message = "内容不能为空")
    private String context;	// 内容

    @NotNull(message = "耗时不能为空")
    private Double costTime;	// 耗时

    @Schema(description = "是否删除，true 表示删除")
    private Boolean remove;

    
}
