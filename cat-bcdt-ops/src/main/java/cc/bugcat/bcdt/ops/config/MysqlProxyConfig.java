package cc.bugcat.bcdt.ops.config;


import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author bugcat-cc
 */
@Data
@Component
@ConfigurationProperties(prefix = "mysql.proxy")
public class MysqlProxyConfig implements InitializingBean {
    
    private boolean enable = false; //是否启用

    private int writeThread = 3;	// 写sql日志线程数量
    

    @Override
    public void afterPropertiesSet()  {
    }
    

}
