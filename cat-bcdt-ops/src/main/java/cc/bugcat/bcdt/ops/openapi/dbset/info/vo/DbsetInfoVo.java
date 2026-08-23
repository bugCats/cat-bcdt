package cc.bugcat.bcdt.ops.openapi.dbset.info.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema
public class DbsetInfoVo {

    @Schema(description = "数据id")
    private Long id;

    @Schema(description = "数据库名")
    private String code;

    @Schema(description = "数据库描述")
    private String desc;

    @Schema(description = "jdbc链接：jdbc:mysql://127.0.0.1:3306/dbname")
    private String jdbcUrl;

    @Schema(description = "数据库账号")
    private String username;

    @Schema(description = "数据库密码")
    private String password;

    @Schema(description = "驱动")
    private String driverName;

    @Schema(description = "数据库字段加密方式：sm4")
    private String cryptoType;

    @Schema(description = "数据库字段加密key")
    private String cryptoKey;


}
