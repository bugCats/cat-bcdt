package cc.bugcat.bcdt.logsev.config;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 系统相关配置
 * @author bugcat-cc
 * */
@Component
public class AppConfig implements InitializingBean {
	
	
	private static AppConfig config;

	
	@Value("${spring.application.name}")
	private String name;		//系统名

	
	@Value("${app.env}")
	private String env;			//环境：dev:开发;test:测试;prod:生产
	
	
	@Value("${app.host}")
	private String appHost;		//服务所在ip

	
	@Value("${app.bcdt-path}")
	private String bcdtPath;

	
	/**
	 * 内存溢出后输出堆文件的目录
	 * */
	@Value("${heap-dump.dir-path}")
	private String heapDumpDirPath;
	
	
	/**
	 * 是否监听堆文件目录
	 * */
	@Value("${heap-dump.listen-enable:false}")
	private boolean heapDumpListenEnable;
	
	
	/**
	 * 内存溢出后，通知钉钉群token
	 * */
	@Value("${heap-dump.ding-token:}")
	private String heapDumpDingToken;
	
	
	
	
	
	@Override
	public void afterPropertiesSet() {
		config = this;
	}

	
	public static AppConfig getConfig() {
		return config;
	}
	

	public String getName() {
		return name;
	}
	public String getEnv() {
		return env;
	}
	public String getAppHost() {
		return appHost;
	}
	public String getBcdtPath() {
		return bcdtPath;
	}
	public String getHeapDumpDirPath() {
		return heapDumpDirPath;
	}
	public boolean isHeapDumpListenEnable() {
		return heapDumpListenEnable;
	}
	public String getHeapDumpDingToken() {
		return heapDumpDingToken;
	}
}