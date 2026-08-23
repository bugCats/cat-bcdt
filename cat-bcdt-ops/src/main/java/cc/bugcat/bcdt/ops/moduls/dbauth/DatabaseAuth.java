package cc.bugcat.bcdt.ops.moduls.dbauth;

import cc.bugcat.bcdt.ops.service.mysql.spi.IUserAuthInfo;
import lombok.Data;

/**
 * 数据库授权信息
 * @author bugcat-cc
 */
@Data
public class DatabaseAuth implements IUserAuthInfo {
    
    private String realname;        //姓名
    
    private String reason;      //原因
    
    private String ipAddr;      //申请IP
    
    private String authCode;    // account#授权码

    private String account;     //账户
    
    private String userCode;    //子账号 username@code
    
    private String userPwd;     //子账号密码
    
    private String write;       //1读写权限,0只读

    private boolean dba;    //是否是dba

}
