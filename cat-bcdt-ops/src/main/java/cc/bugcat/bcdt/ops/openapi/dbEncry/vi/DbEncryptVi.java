package cc.bugcat.bcdt.ops.openapi.dbEncry.vi;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Schema
@Data
public class DbEncryptVi {

    @Schema(description = "数据库Id")
    @NotBlank(message = "数据库Id不能为空")
    private String dbId;
    
    @Schema(description = "加解密：decrypt 解密；encrypt 加密")
    @NotBlank(message = "加解密类型不能为空")
    private String codingType;
    
    @Schema(description = "需要加解密的字符串")
    @NotBlank(message = "需要加解密的字符串不能为空")
    private String sourceCode;

    
}
