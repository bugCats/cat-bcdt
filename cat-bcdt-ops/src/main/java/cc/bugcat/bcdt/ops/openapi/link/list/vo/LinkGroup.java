package cc.bugcat.bcdt.ops.openapi.link.list.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class LinkGroup {
    
    private String groupName;
    
    private List<LinkInfo> infoValue;
    
    @JsonIgnore
    private String id;
    
}
