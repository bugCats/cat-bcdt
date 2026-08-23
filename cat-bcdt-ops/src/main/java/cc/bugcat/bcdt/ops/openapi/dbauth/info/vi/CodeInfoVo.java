package cc.bugcat.bcdt.ops.openapi.dbauth.info.vi;


import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema
public class CodeInfoVo {
    
    @Schema(description = "读子账号")
    private String readUserCode = "";
    
    @Schema(description = "读子账号密码")
    private String readUserPwd = "";
    
    @Schema(description = "写子账号")
    private String writeUserCode = "";
    
    @Schema(description = "写子账号密码")
    private String writeUserPwd = "";

    
}
