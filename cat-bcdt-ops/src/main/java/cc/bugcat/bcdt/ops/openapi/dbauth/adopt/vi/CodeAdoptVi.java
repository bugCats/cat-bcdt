package cc.bugcat.bcdt.ops.openapi.dbauth.adopt.vi;


import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema
public class CodeAdoptVi {
    
    @Schema(description = "账号")
    private String account;
    
    @Schema(description = "授权码")
    private String code;
    
}
