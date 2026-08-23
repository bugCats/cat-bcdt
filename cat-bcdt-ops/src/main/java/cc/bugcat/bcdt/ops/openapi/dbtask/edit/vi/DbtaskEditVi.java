package cc.bugcat.bcdt.ops.openapi.dbtask.edit.vi;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;


@Data
@Schema
public class DbtaskEditVi{
    
    @Schema(description = "操作类型：2 删除；")
    private String type;
    
    @Schema(description = "主键id")
    private Long id;
}
