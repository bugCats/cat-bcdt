package cc.bugcat.bcdt.ops.openapi.user.login.vo;

import cc.bugcat.acore.framework.spi.IUserInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;


@Data
public class UserInfo implements Serializable, IUserInfo {

    @Serial
    private static final long serialVersionUID = 972245132000000004L;
    
    private String token;
    
    private String account;    //禅道账户
    
    private String realname;    //姓名
    
    private Set<String> opers;  //平台权限




    private String phone;       //手机号
    
    
    /**
     * 登录ip
     * */
    @JsonIgnore
    private String ipAddr;

    /**
     * 登录时间
     * */
    @JsonIgnore
    private String loginTime;

    
}
