package cc.bugcat.bcdt.ops.openapi.dbauth.apply.vi;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Schema
@Data
public class CodeApplyVi {
    
    @Schema(description = "申请原因")
    @NotBlank(message = "申请原因不能为空")
    private String reason;
    
    
    @Schema(description = "1读写权限，0读权限")
    @NotBlank(message = "权限不能为空")
    private String write;
    
}
