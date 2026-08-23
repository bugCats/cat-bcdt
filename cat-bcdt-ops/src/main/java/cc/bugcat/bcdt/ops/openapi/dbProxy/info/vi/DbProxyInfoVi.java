package cc.bugcat.bcdt.ops.openapi.dbProxy.info.vi;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema
public class DbProxyInfoVi {

    @Schema(description = "数据id")
    @NotNull(message = "数据id不能为空")
    private Long id;

}
