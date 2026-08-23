package cc.bugcat.bcdt.ops.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 配置静态资源
 * 
 * @author bugcat-cc
 */
@Component
public class WebStaticResourceConfig implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(WebStaticResourceConfig.class);
    
    @Autowired
    private AppConfig appConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if ( StringUtils.isNotBlank(appConfig.getBcdtLocalPath()) ) {
            String webapps = "file:///" + appConfig.getBcdtLocalPath();
            registry.addResourceHandler("/web/**").addResourceLocations(webapps);
            logger.warn("webapps={}", webapps);
        }
    }
}
