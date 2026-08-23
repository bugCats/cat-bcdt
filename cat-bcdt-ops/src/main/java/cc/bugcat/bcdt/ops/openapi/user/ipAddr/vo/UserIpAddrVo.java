package cc.bugcat.bcdt.ops.openapi.user.ipAddr.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class UserIpAddrVo {

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "ip地址")
    private String ipAddr;

    @Schema(description = "登录时间")
    private String loginTime;
    
}
