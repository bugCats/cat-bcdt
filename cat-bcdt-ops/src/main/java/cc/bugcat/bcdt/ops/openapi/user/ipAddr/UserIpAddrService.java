package cc.bugcat.bcdt.ops.openapi.user.ipAddr;

import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.openapi.user.ipAddr.vo.UserIpAddrVo;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class UserIpAddrService extends AbstractOpenApiService<Void, List<UserIpAddrVo>> {
    
    @Override
    public List<UserIpAddrVo> execute(Void reqVi, RequestContext context) {
        List<UserInfo> userInfos = UserUtil.getAllUser();
        List<UserIpAddrVo> list = new ArrayList<>(userInfos.size());
        for ( UserInfo userInfo : userInfos ) {
            UserIpAddrVo info = new UserIpAddrVo();
            info.setName(userInfo.getRealname());
            info.setIpAddr(userInfo.getIpAddr());
            info.setLoginTime(userInfo.getLoginTime());
            list.add(info);
        }
        return list;
    }
}
