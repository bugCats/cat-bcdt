package cc.bugcat.bcdt.ops.openapi.user.login.vi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Schema
@Data
public class UserLoginVi {

    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    
    @JsonIgnore
    private String ipAddr;

}
