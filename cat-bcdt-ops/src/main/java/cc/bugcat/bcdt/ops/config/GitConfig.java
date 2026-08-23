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
@ConfigurationProperties(prefix = "git")
public class GitConfig implements InitializingBean {

    private static GitConfig gitConfig;

    private String host;

    
    private String username;
    
    // token
    private String token;
    
    
    @Override
    public void afterPropertiesSet() {
        gitConfig = this;
    }

    
    

}
