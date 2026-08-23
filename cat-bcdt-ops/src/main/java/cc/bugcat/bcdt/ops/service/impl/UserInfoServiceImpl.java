package cc.bugcat.bcdt.ops.service.impl;

import cc.bugcat.acore.framework.constants.Const;
import cc.bugcat.acore.framework.spi.IUserInfo;
import cc.bugcat.acore.webmvc.spi.IUserInfoService;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {

    @Override
    public IUserInfo preUserInfo(HttpServletRequest request) {
        String token = request.getHeader(Const.userToken);
        if ( StringUtils.isBlank(token) ) {
            return null;
        }

        UserInfo userInfo = UserUtil.getUserByToken(token);
        request.setAttribute(Const.userToken, userInfo);
        if ( userInfo != null ){
            UserUtil.refresh(userInfo);
        }
        return userInfo;
    }

    
    @Override
    public IUserInfo getUserInfo(HttpServletRequest request) {
        return (IUserInfo) request.getAttribute(Const.userToken);
    }
}
