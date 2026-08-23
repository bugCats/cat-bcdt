package cc.bugcat.bcdt.ops.openapi.dbProxy.status.vi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;


@Data
@Schema
public class DbProxyStatusVi {


    @Schema(description = "数据id")
    private Long id;


    @Schema(description = "是否启用：0禁用；1启用")
    private String enable;

}
