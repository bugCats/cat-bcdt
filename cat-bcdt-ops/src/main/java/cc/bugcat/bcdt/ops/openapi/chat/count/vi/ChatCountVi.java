package cc.bugcat.bcdt.ops.openapi.chat.count.vi;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema
public class ChatCountVi {
    
    @Schema(description = "每次获取数量")
    private Integer pageSize;
    
    @Schema(description = "上一次最后一条数据id")
    private Long minId;
    
    @Schema(description = "消息分类")
    private Integer msgType;	// 消息分类
    
    @Schema(description = "自定义标记")
    private String msgTags;	// 消息标记
    
    @Schema(description = "0未读，1已读")
    private String read;	// 0未读，1已读
    
    @Schema(description = "查询日期范围")
    private Integer dateRange;

}
