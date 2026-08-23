package cc.bugcat.bcdt.ops.service.mysql;


import cc.bugcat.bcdt.ops.config.MysqlProxyConfig;
import cc.bugcat.bcdt.ops.service.mysql.handler.SessionManager;
import cc.bugcat.bcdt.ops.service.mysql.logger.MysqlCoderServiceImpl;
import cc.bugcat.bcdt.ops.service.mysql.logger.SqlLoggerServiceImpl;
import cc.bugcat.bcdt.ops.service.mysql.user.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;

/**
 *
 *
 * @author bugcat-cc
 */
@ConditionalOnProperty("mysql.proxy.enable")
@Component
@EnableConfigurationProperties(MysqlProxyConfig.class)
@Import({MysqlCoderServiceImpl.class, SqlLoggerServiceImpl.class, DatabaseProxyStarter.class})
public class EnableMysqlProxy implements InitializingBean {

    public final static Logger LOGGER = LoggerFactory.getLogger(EnableMysqlProxy.class);
    public final static Charset CHARSET = Charset.forName(System.getProperty("charset-name", "utf-8"));

    
    @Override
    public void afterPropertiesSet() {
        LOGGER.info("DatabaseProxyStarter 启用！");
        
        
        
//        //测试账号
//        UserSession user = UserSession.builder()
//                .userCode("bugcat@123465")    //子账号 username@code
//                .userPwd("bugcatcc")           //子账号密码
//                .dba(true)
//                .write("1")         //读写
//                .logFile(new File("E:\\tmp\\log.txt"))
//                .build();
//        SessionManager manager = SessionManager.getManager();
//        manager.register(user);
    }

    
}
