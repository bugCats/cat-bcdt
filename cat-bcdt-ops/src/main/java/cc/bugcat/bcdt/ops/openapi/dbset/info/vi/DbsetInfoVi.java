package cc.bugcat.bcdt.ops.openapi.dbset.info.vi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema
public class DbsetInfoVi {

    
    @Schema(description = "数据id")
    @NotNull(message = "数据id不能为空")
    private Long id;

}
