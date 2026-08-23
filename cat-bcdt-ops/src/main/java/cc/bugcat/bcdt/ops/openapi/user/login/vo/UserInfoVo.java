package cc.bugcat.bcdt.ops.openapi.user.login.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema
public class UserInfoVo extends UserInfo {


    @JsonIgnore
    private String phone;       //手机号
    
}
