package cc.bugcat.bcdt.ops.openapi.dbtask.list.vi;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema
public class DbtaskListVi{
    
    @NotBlank(message = "开始日期不能为空")
    @Schema(description = "开始日期")
    private String startDate;
    
    @NotBlank(message = "结束日期不能为空")
    @Schema(description = "结束日期")
    private String endDate;
    
    @Schema(description = "数据库")
    private Long dbId;
    
    @Schema(description = "状态：10初始；50已执行；90执行失败")
    private String status;	// 状态：10初始；50已执行；90执行失败

}
