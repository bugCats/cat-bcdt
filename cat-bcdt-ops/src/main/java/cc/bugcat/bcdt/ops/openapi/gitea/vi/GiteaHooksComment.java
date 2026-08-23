package cc.bugcat.bcdt.ops.openapi.gitea.vi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;


@Data
@Schema
public class GiteaHooksComment {

    @Schema(description = "操作类型")
    @JsonProperty("action")
    private String action;  // opened edited closed

    @Schema(description = "issue内容")
    @JsonProperty("issue")
    private GitIssue issue;

    @Schema(description = "评论内容")
    @JsonProperty("comment")
    private GitComment comment;
    
    
    @Data
    @Schema
    public static class GitIssue {

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
    
    
    @Data
    @Schema
    public static class GitComment {
        
        @Schema(description = "评论人")
        @JsonProperty("user")
        private GiteaUser user;

        @Schema(description = "评论内容")
        @JsonProperty("body")
        private String body;

    }

    
}
