package cc.bugcat.bcdt.ops.openapi.user.sso.vo;


import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema
public class UserSsoTokenVo {
    
    @Schema(description = "sso token")
    private String token;
    
    @Schema(description = "账号")
    private String account;
    
}
