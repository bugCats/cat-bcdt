package cc.bugcat.bcdt.ops.openapi.dbtask.save.vi;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Schema
public class DbtaskSaveVi {
    
    @NotNull(message = "数据库不能为空")
    @Schema(description = "数据库")
    private Long dbId;	// 数据库
    
    @NotBlank(message = "sql语句不能为空")
    @Schema(description = "执行的sql语句")
    private String sql;	// 执行的sql语句
    
    @Schema(description = "sql描述")
    private String desc;
    
    @NotBlank(message = "计划执行时间不能为空")
    @Pattern(regexp = "^@?[0-9]{2}:00$", message = "计划执行时间格式错误，HH:mm")
    @Schema(description = "计划执行时间，HH:mm")
    private String taskDate;
    
}
