package cc.bugcat.bcdt.ops.openapi.chat.read.vi;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;


@Data
@Schema
public class ChatReadVi {

    @Schema(description = "消息id")
    private Long msgId;

    @Schema(description = "消息分类")
    private Integer msgType;

    @Schema(description = "是否全部标记已读")
    private boolean readAll;


}
