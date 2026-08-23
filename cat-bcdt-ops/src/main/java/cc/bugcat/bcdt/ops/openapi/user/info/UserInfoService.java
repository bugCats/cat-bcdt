package cc.bugcat.bcdt.ops.openapi.user.info;

import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfoVo;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class UserInfoService extends AbstractOpenApiService<Void, UserInfoVo> {

	@Override
	public UserInfoVo execute (Void reqVi, RequestContext context) {
		UserInfo user = UserUtil.getUser(context);
		return BeanCopyUtil.copy(user, UserInfoVo::new);
	}
}
