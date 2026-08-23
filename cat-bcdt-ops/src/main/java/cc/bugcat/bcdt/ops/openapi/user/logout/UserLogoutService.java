package cc.bugcat.bcdt.ops.openapi.user.logout;

import cc.bugcat.acore.framework.constants.Const;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class UserLogoutService extends AbstractOpenApiService<Void, Void> {

	@Override
	@Transactional(readOnly = true)
	public Void execute (Void reqVi, RequestContext context) {
		HttpServletRequest request = context.getRequest();
		String token = request.getHeader(Const.userToken);
		if ( StringUtils.isBlank(token) ) {
			return null;
		}
		UserInfo userInfo = UserUtil.getUserByToken(token);
		if ( userInfo == null ){
			return null;
		}
		
		UserUtil.removeUser(userInfo.getAccount(), token);
		
		return null;
	}
}
