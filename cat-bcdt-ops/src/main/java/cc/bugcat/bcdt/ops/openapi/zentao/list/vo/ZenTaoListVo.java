package cc.bugcat.bcdt.ops.openapi.zentao.list.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class ZenTaoListVo {


    @Schema(description = "数据id")
    private Long objectId;

    @Schema(description = "名称")
    private String objectName;

    @Schema(description = "类型：10 task, 20 bug，30 需求，40 用例")
    private Integer objectType;

    
}
