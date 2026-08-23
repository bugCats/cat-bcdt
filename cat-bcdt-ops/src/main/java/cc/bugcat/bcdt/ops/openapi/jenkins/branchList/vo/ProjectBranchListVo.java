package cc.bugcat.bcdt.ops.openapi.jenkins.branchList.vo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
@Schema
public class ProjectBranchListVo {
    
    @Schema(description = "开发、测试环境")
    private String groupName;
    
    @Schema(description = "文件夹-仓库信息")
    private List<GroupProject> groupProjects;
    
    @JsonIgnore
    private Map<String, List<Depository>> dictDeps;

}
