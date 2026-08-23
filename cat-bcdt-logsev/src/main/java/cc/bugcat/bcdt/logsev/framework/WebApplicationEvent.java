package cc.bugcat.bcdt.logsev.framework;

import cc.bugcat.acore.framework.spi.IWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 系统启动、关闭执行
 *
 * @author bugcat-cc
 * */
@Component
public class WebApplicationEvent implements IWebApplication  {


	public void onStart(ApplicationContext applicationContext){
		System.out.println(">>>>>>> onStart");
	}

	
}
