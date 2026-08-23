package cc.bugcat.bcdt.ops.openapi.chat.list.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.Date;


@Data
@Schema
public class ChatListVo {

    @Schema(description = "消息id")
    private Long id;	// 主键

    @Schema(description = "消息归属人")
    private String account;	// 消息归属人

    @Schema(description = "消息归属人名")
    private String realname;	// 消息归属人

    @Schema(description = "消息发送人")
    private String sendAccount;	// 消息发送人

    @Schema(description = "消息发送人名")
    private String sendRealname;	// 消息发送人

    @Schema(description = "消息内容  html")
    private String content;	// 消息内容

    @Schema(description = "消息分类")
    private Integer msgType;	// 消息分类

    @Schema(description = "自定义标记")
    private String msgTags;	// 消息标记

    @Schema(description = "0未读，1已读")
    private String read;	// 0未读，1已读

    @Schema(description = "发送时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date savedate;	// 发送时间

    
}
