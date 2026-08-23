package cc.bugcat.bcdt.ops.openapi.link.list.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema
public class LinkColumn {
    
    private String columnName;
    
    private List<LinkLabel> labelValue;
    
}
