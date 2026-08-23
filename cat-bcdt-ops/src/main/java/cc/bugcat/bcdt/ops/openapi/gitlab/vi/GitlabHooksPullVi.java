package cc.bugcat.bcdt.ops.openapi.gitlab.vi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.List;


@Data
@Schema
public class GitlabHooksPullVi {

    @Schema(description = "操作类型")
    @JsonProperty("event_type")
    private String eventType;  // merge_request

  
    @Schema(description = "触发人")
    @JsonProperty("user")
    private GitlabUser user;  
    
    
    @Schema(description = "事件信息")
    @JsonProperty("object_attributes")
    private ObjectAttributes objectAttributes;


    @JsonProperty("assignees")
    private List<GitlabUser> assignees;


    
    
    
    /**
     * gitlab账号
     * */
    @Data
    @Schema
    public static class GitlabUser {
    
        private String id;
        
        private String username;    // bugcat
        
        private String name;        //bugcat
        
    }
    
    
    @Data
    @Schema
    public static class ObjectAttributes {
        
        @Schema(description = "指派人id")
        @JsonProperty("assignee_id")
        private String assigneeId;
        
        @Schema(description = "操作人id")
        @JsonProperty("author_id")
        private String authorId;

        @Schema(description = "合并请求标题")
        private String title;

        @Schema(description = "合并请求描述")
        private String description;
        
        @Schema(description = "合并id")
        private String iid;

        @Schema(description = "合并url")
        private String url;


        @Schema(description = "当前状态")
        private String state;   //merged

        @Schema(description = "操作")
        private String action;  //merge

    }
    
}
