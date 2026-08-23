package cc.bugcat.bcdt.ops.openapi.gitea.vi;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema
public class GiteaUser {

    @Schema(description = "登录名 bugcat")
    private String username;

    @Schema(description = "真实名称 张三")
    private String fullName;

    
}
