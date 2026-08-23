package cc.bugcat.bcdt.logsev.modules.account;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 *
 * @author bugcat-cc
 * */
@Component
public class AccountSessionFactory {
    
    private final ConcurrentMap<String, AccountSession> sessionMap = new ConcurrentHashMap<>();
    private int second = 0;

    
    /**
     * 每秒执行一次
     * */
    @Scheduled(fixedRate = 1000)
    public void eachSecond() {
        
        // 如果客户端开启了实时读取，则触发输出服务端内容
        this.sessionMap.forEach((key, value) -> {
            ReaderFileSessionFactory factory = value.getReaderFactory();
            factory.readerAtTime(value);
        });
        
        // 每10s检查一次用户会话
        if (this.second ++ >= 10) {
            this.second = 0;
            
            Iterator<Map.Entry<String, AccountSession>> iterator = this.sessionMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, AccountSession> entry = iterator.next();
                AccountSession session = entry.getValue();
                if (!session.checkAlive()) continue;
                iterator.remove();
            }
        }
    }
    
    
    /**
     * 开启用户会话
     * */
    public AccountSession openSession(String userId) {
        AccountSession session = this.sessionMap.get(userId);
        if (session == null) {
            session = new AccountSession();
            this.sessionMap.putIfAbsent(userId, session);
        }
        return session;
    }

    
    /**
     * 根据userId获取用户会话对象
     * */
    public AccountSession getSession(String userId) {
        AccountSession session = this.sessionMap.get(userId);
        if (session == null) {
            throw BusinessException.builder("403", Errors.SYSTEM_NOT_LOGIN);
        }
        return session;
    }
}

