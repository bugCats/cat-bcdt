package cc.bugcat.bcdt.ops.openapi.dbset.list.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class DbsetListVo {

    @Schema(description = "数据id")
    private Long id;

    @Schema(description = "数据库名")
    private String code;

    @Schema(description = "数据库描述")
    private String desc;

    @Schema(description = "数据库字段加密方式：sm4")
    private String cryptoType;

}
