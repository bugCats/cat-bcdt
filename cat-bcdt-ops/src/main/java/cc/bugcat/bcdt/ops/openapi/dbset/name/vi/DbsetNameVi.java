package cc.bugcat.bcdt.ops.openapi.dbset.name.vi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
@Schema
public class DbsetNameVi {

    @Schema(description = "数据库字段加密方式")
    private String cryptoType;

}
