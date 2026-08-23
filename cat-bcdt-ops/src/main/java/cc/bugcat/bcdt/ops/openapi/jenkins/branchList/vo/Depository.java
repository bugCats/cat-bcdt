package cc.bugcat.bcdt.ops.openapi.jenkins.branchList.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema
public class Depository {
    
    @Schema(description = "仓库名称")
    private String projectName;
    
    @Schema(description = "分支名称")
    private String branchName;
    
    @Schema(description = "配置文件路径")
    private String configPath;
    
    @Schema(description = "最后修改人")
    private String editUser;
    
}
