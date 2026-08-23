package cc.bugcat.bcdt.ops.service.mysql.handler;

import cc.bugcat.acore.framework.cache.spi.InnerCache;
import cc.bugcat.acore.framework.constants.Const;
import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.dao.zentao.BcDbAuthDao;
import cc.bugcat.bcdt.ops.model.zentao.BcDbAuth;
import cc.bugcat.bcdt.ops.moduls.dbauth.DatabaseAuth;
import cc.bugcat.bcdt.ops.openapi.dbauth.info.vi.CodeInfoVo;
import cc.bugcat.bcdt.ops.service.mysql.spi.IUserAuthCacheService;
import cc.bugcat.bcdt.ops.service.mysql.spi.IUserAuthInfo;
import cc.bugcat.bcdt.ops.service.mysql.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class SimpleUserAuthCacheService implements IUserAuthCacheService {


    @Autowired
    private BcDbAuthDao bcDbAuthDao;
    
    private InnerCache cache = InnerCache.getCache("dbauth:");

    
    /**
     * 得到授权码
     * */
    @Override
    public DatabaseAuth codeApply(String userCode, Consumer<DatabaseAuth> consumer) {
        DatabaseAuth auth = cache.get(userCode, DatabaseAuth.class);
        if( auth == null ){ //之前不存在
            auth = new DatabaseAuth();
        } else {    //之前存在授权，删除之前授权
            cache.delete(auth.getUserCode());
            cache.delete(auth.getAuthCode());
        }
        consumer.accept(auth);
        cache.set(auth.getUserCode(), InnerCache.min * 10, auth);
        cache.set(auth.getAuthCode(), InnerCache.min * 10, auth);
        
        return auth;
    }


    @Override
    public void codeValid(String authCode, String ip) {
        DatabaseAuth auth = cache.get(authCode, DatabaseAuth.class);
        if ( auth == null ) {
            throw BusinessException.builder("500", Errors.SYS_ERR, "未知的授权码");
        }
        cache.delete(auth.getUserCode());
        cache.delete(auth.getAuthCode());

        String tomcatHome = System.getProperty("catalina.base");
        String day = DateUtil.rs.format(new Date(), "yyyyMMdd");
        String logs = tomcatHome + "/dbauth/" + day.substring(0, 4) + "/" + day;
        String path = logs + "/" + ObjectUtil.uuid() + ".txt";

        File logsDir = new File(logs);
        logsDir.mkdirs();

        File logFile = new File(path);
        if ( !logFile.exists() ) {
            try { logFile.createNewFile();} catch ( Exception ex ) {}
        }

        UserSession user = UserSession.builder()
                .userCode(auth.getUserCode())
                .userPwd(auth.getUserPwd())
                .dba(auth.isDba())
                .write(auth.getWrite())
                .logFile(logFile)
                .build();

        SessionManager manager = SessionManager.getManager();
        manager.register(user);
        
        Consumer<String> writeLogs = user.getWriteLogs();
        writeLogs.accept("----------------- 授权 -----------------" + Const.NEW_LINE);

        BcDbAuth dbAuth = new BcDbAuth();
        dbAuth.setAccount(auth.getAccount());
        dbAuth.setRealname(auth.getRealname());
        dbAuth.setReason(auth.getReason());
        dbAuth.setIpAddr(ip);
        dbAuth.setPath(path);
        dbAuth.setCreateDate(new Date());
        bcDbAuthDao.insert(dbAuth);
    }


    @Override
    public CodeInfoVo myAuthInfo(String account) {
        CodeInfoVo vo = new CodeInfoVo();
        
        SessionManager manager = SessionManager.getManager();

        List<IUserAuthInfo> reads = cache.getPrefix(account + "@r", IUserAuthInfo.class);
        
        if( ObjectUtil.isNotEmpty(reads) ){
            IUserAuthInfo read = reads.getFirst();
            vo.setReadUserCode(read.getUserCode());
            vo.setReadUserPwd(read.getUserPwd());
        } else {
            UserSession user = manager.getPrefix(account + "@r");
            if( user != null ){
                vo.setReadUserCode(user.getUserCode());
                vo.setReadUserPwd(user.getUserPwd());
            }
        }
        List<IUserAuthInfo> writes = cache.getPrefix(account + "@w", IUserAuthInfo.class);
        if( ObjectUtil.isNotEmpty(writes) ){
            IUserAuthInfo write = writes.getFirst();
            vo.setWriteUserCode(write.getUserCode());
            vo.setWriteUserPwd(write.getUserPwd());
        } else {
            UserSession user = manager.getPrefix(account + "@w");
            if( user != null ){
                vo.setWriteUserCode(user.getUserCode());
                vo.setWriteUserPwd(user.getUserPwd());
            }
        }
        return vo;
    }
    
}
