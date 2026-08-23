package cc.bugcat.bcdt.ops.openapi.gitea.vi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema
public class GiteaHooksPull {

    @Schema(description = "操作类型")
    @JsonProperty("action")
    private String action;  // opened edited closed

    @Schema(description = "钩子类型")
    @JsonProperty("pull_request")
    private GitPullRequest pullRequest;

    @Schema(description = "操作人")
    @JsonProperty("sender")
    private GiteaUser sender;
    
    
    @Data
    @Schema
    public static class GitPullRequest {

        @Schema(description = "申请人")
        @JsonProperty("user")
        private GiteaUser user;      //申请人

        @Schema(description = "合并标题")
        @JsonProperty("title")
        private String title;   //合并标题

        @Schema(description = "详细内容")
        @JsonProperty("body")
        private String body;    //详细内容

        @Schema(description = "指定合并人")
        @JsonProperty("assignee")
        private GiteaUser assignee;  //指定合并人

        @Schema(description = "当前状态")
        @JsonProperty("state")
        private String state;   //当前状态

        @Schema(description = "合并请求id")
        @JsonProperty("html_url")
        private String htmlUrl;    //合并请求id

        @Schema(description = "合并人")
        @JsonProperty("merged_by")
        private GiteaUser mergedBy;


    }

}
