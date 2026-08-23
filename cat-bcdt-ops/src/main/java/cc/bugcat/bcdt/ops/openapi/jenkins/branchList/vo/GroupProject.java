package cc.bugcat.bcdt.ops.openapi.jenkins.branchList.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.List;

@Data
@Schema
public class GroupProject {
    
    @Schema(description = "文件夹名称")
    private String dirName;
    
    @Schema(description = "仓库信息")
    private List<Depository> dictDeps;
    
}
