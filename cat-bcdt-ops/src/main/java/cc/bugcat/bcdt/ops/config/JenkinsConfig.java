package cc.bugcat.bcdt.ops.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author bugcat-cc
 */
@Data
@Component
@ConfigurationProperties(prefix = "jenkins")
public class JenkinsConfig {


    private String host;    // 192.168.3.90:9009
    
    private String auth;    // edzjb2RlOnlqY29kZQ==
    
    private String plugin;  // 插件url

    
}
