package cc.bugcat.bcdt.ops.openapi.link.list.vo;


import cc.bugcat.acore.framework.json.JsonTools;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema
public class LinkLabel {
    
    private String labelName;
    
    private String type;
    
    private String className; // 类名
    
    private JsonTools extra;
    
    private List<LinkGroup> groupValue;
    
    @JsonIgnore
    private String id;
    
}
