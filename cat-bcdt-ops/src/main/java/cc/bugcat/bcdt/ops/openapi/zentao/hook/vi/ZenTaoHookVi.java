package cc.bugcat.bcdt.ops.openapi.zentao.hook.vi;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema
public class ZenTaoHookVi {

    @Schema(description = "业务类型")
    private String objectType;  // bug story需求 task任务 testtask测试任务 t0d0 待办

    @Schema(description = "数据id")
    private Long objectID;  // 数据id

    @Schema(description = "触发动作")
    private String action; //动作

    @Schema(description = "操作人")
    private String actor;   //操作人

    @Schema(description = "内容")
    private String comment; //内容

    @Schema(description = "禅道拼接好的内容")
    private String text;    //禅道拼接好的内容

}
