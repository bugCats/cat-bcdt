package cc.bugcat.bcdt.ops.openapi.link.list.vo;

import cc.bugcat.acore.framework.json.JsonTools;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema
public class LinkInfo {
    
    @Schema(description = "数据类型：label；label-cpt；link；link-more；new-line；text")
    private String type;	// 
    
    @Schema(description = "显示内容")
    private String label;	// 
    
    @Schema(description = "title提示")
    private String labelTitle;	// 
    
    @Schema(description = "样式")
    private String className; // 类名
    
    @Schema(description = "描述")
    private String desc;	// 
    
    @Schema(description = "html扩展")
    private JsonTools extra;	// 
    
    @Schema(description = "更多链接")
    private List<LinkInfo> linkMore;
    
    @JsonIgnore
    private String id;	// 

}
