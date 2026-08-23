package cc.bugcat.bcdt.ops.service.mysql.spi;


import cc.bugcat.bcdt.ops.moduls.dbauth.DatabaseAuth;
import cc.bugcat.bcdt.ops.openapi.dbauth.info.vi.CodeInfoVo;

import java.util.function.Consumer;


/**
 *
 *
 * @author bugcat-cc
 */
public interface IUserAuthCacheService {


    /**
     * 获取授权码
     * */
    DatabaseAuth codeApply(String userCode, Consumer<DatabaseAuth> consumer);
    
    /**
     * 确认授权，注入tcp用户session
     * */
    void codeValid(String authCode, String ip);
    
    /**
     * 获取我的授权信息
     * */
    CodeInfoVo myAuthInfo(String account);


}
