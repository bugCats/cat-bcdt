package cc.bugcat.bcdt.ops.openapi.chat.count.vo;


import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;


@Data
@Schema
public class ChatCountVo {

    @Schema(description = "消息类型")
    private Integer msgType;

    @Schema(description = "总数")
    private int total;

    @Schema(description = "已读数量")
    private int read;

    @Schema(description = "未读数量")
    private int unread;


}
