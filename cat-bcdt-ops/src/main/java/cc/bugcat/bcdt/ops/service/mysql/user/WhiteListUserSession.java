package cc.bugcat.bcdt.ops.service.mysql.user;

import java.util.function.Consumer;

/**
 * 白名单中的数据库账号
 *
 *
 * @author bugcat-cc
 */
public class WhiteListUserSession extends UserSession {
    
    public WhiteListUserSession(Builder builder) {
        super(builder);
    }

    /**
     * 不记录
     * */
    @Override
    public Consumer<String> getWriteLogs() {
        return content -> {};
    }
    
    
}
