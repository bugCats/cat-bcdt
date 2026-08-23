package cc.bugcat.bcdt.ops.openapi.dbsql.list.vi;


import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
@Schema
public class DbsqlListVi {


    @Schema(description = "主键，多个用空格、逗号、分行隔开")
    private String id;

    @Schema(description = "描述")
    private String desc;
    
    @NotBlank(message = "开始日期不能为空")
    @Schema(description = "开始日期")
    private String startDate;

    @NotBlank(message = "结束日期不能为空")
    @Schema(description = "结束日期")
    private String endDate;
    
    @Schema(description = "数据库")
    private Long dbId;

    @Schema(description = "状态：执行状态：10 初始；90 已同步；")
    private String status;

    @Schema(description = "仅看自己")
    private boolean onlyMe;

}
