package cc.bugcat.bcdt.ops.openapi.dbauth.apply.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;


@Data
@Schema
public class CodeApplyVo {

    @Schema(description = "子账号 username@code")
    private String userCode;

    @Schema(description = "子账号密码")
    private String userPwd;

    
}
