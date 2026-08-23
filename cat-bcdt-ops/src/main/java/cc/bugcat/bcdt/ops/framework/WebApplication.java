package cc.bugcat.bcdt.ops.framework;

import cc.bugcat.acore.framework.spi.IWebApplication;
import cc.bugcat.bcdt.ops.config.AppConfig;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 系统启动、关闭执行
 *
 * @author bugcat-cc
 */
@Component
public class WebApplication implements IWebApplication {


	/**
	 * 当web启动时执行
	 * */
	@Override
	public void onStart(ApplicationContext context) {
		if( "local".equals(AppConfig.getConfig().getEnv()) ){
			UserInfo userInfo = UserUtil.defaultUser();
			UserUtil.setUserInfo(userInfo);
		}
	}

}
