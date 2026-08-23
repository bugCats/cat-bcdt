package cc.bugcat.bcdt.ops.openapi.dbProxy.list.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class DbProxyListVo {

    @Schema(description = "数据id")
    private Long id;

    @Schema(description = "数据库名")
    private String name;

    @Schema(description = "数据库描述")
    private String desc;

    @Schema(description = "绑定到本地端口")
    private Integer localPort;

    @Schema(description = "是否启用：0禁用；1启用；")
    private String enable;
    
    @Schema(description = "是否启用")
    private String enableName;

}
