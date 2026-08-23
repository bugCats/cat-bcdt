package cc.bugcat.bcdt.ops.openapi.dbsql.edit.vi;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema
public class DbsqlEditVi {
    
    @Schema(description = "操作类型：1 执行；2 删除；3 同步")
    private String type;
    
    @Schema(description = "主键id")
    private Long id;

}
