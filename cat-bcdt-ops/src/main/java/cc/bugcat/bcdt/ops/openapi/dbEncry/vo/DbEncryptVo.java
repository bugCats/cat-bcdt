package cc.bugcat.bcdt.ops.openapi.dbEncry.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;


@Schema
@Data
public class DbEncryptVo {
    
    @Schema(description = "输入内容")
    private String source;
    
    @Schema(description = "输出内容")
    private String coding;
    
}
