package cc.bugcat.bcdt.ops.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author bugcat-cc
 */
@Data
@Component
@ConfigurationProperties(prefix = "sso")
public class SsoConfig {

    
    private String sonarQube;
    
    
    
    
}
