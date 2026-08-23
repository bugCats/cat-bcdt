package cc.bugcat.bcdt.ops.openapi.dbsql.save.vi;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema
public class DbsqlSaveVi {

    @NotNull(message = "数据库不能为空")
    @Schema(description = "数据库")
    private Long dbId;	// 数据库

    @NotBlank(message = "sql语句不能为空")
    @Schema(description = "执行的sql语句")
    private String sql;	// 执行的sql语句

    @Schema(description = "sql描述")
    private String desc;

}
