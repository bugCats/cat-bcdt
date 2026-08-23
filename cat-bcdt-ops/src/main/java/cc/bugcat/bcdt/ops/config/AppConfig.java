package cc.bugcat.bcdt.ops.config;

import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 系统相关配置
 * @author bugcat-cc
 */
@Getter
@Component
public class AppConfig implements InitializingBean {

	@Getter
    private static AppConfig config;


	@Value("${spring.application.name}")
	private String name;//系统名

	@Value("${app.env}")
	private String env;
	
	
	@Value("${crypto-key:}")
	private String cryptoKey; //禅道数据库字段加解密密钥

	
	@Value("${zentao.web-host:}")
	private String zentaoWebHost;
	
	@Value("${bcdt.ops-host}")
	private String bcdtOpsHost;
	
	@Value("${bcdt.local-path:}")
	private String bcdtLocalPath;
	
	
	@Override
	public void afterPropertiesSet() {
		config = this;
	}


}
