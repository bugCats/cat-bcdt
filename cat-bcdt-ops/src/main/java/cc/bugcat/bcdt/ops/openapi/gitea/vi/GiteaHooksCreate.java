package cc.bugcat.bcdt.ops.openapi.gitea.vi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;


@Data
@Schema
public class GiteaHooksCreate {


    @Schema(description = "标签或者分支名称")
    @JsonProperty("ref")
    private String ref;


    @Schema(description = "标签或者分支：tag；branch")
    @JsonProperty("ref_type")
    private String refType;

    
    @Schema(description = "基础分支名称")
    @JsonProperty("default_branch")
    private String defaultBranch;

    @Schema(description = "仓库")
    @JsonProperty("repository")
    private GitRepository repository;
    
    @Schema(description = "操作人")
    @JsonProperty("sender")
    private GiteaUser sender;
    
    
    @Data
    @Schema
    public static class GitRepository {

        @Schema(description = "仓库名称")
        @JsonProperty("name")
        private String name;

    }
    

}
