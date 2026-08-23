package cc.bugcat.bcdt.ops.openapi.jenkins.branchSave.vi;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;


@Data
@Schema
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectBranchSaveVi {

    @Schema(description = "配置文件路径")
    private String fpath;

    @Schema(description = "新分支名")
    private String branch;

    @JsonIgnore
    private String editUser;    //修改人


}
