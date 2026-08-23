package cc.bugcat.bcdt.ops.openapi.zentao.list.vi;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema
public class ZenTaoListVi {
    
    @Schema(description = "搜索关键词")
    private String keyword;

}

