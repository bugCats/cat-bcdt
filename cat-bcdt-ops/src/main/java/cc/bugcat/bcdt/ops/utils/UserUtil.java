package cc.bugcat.bcdt.ops.utils;


import cc.bugcat.acore.framework.cache.spi.InnerCache;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.acore.webmvc.web.RequestExtraInfo;
import cc.bugcat.bcdt.ops.constants.Constants;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 用户缓存类
 *
 * @author bugcat-cc
 */
public class UserUtil {

    private static final InnerCache cache = InnerCache.getCache("user:");
    

    /**
     * 缓存登录用户
     * */
    public static void setUserInfo(UserInfo user) {
        String token = user.getToken();
        String account = user.getAccount();
        
        UserInfo exist = cache.get(Constants.userIdKey + account, UserInfo.class);
        if( exist != null ){
            cache.delete(Constants.userIdKey + account);
            cache.delete(Constants.tokenKey + exist.getToken());
        }

        cache.set(Constants.userIdKey + account, InnerCache.day, user);
        cache.set(Constants.tokenKey + token, InnerCache.day, user);
    }
    
    /**
     * 刷新时间
     * */
    public static void refresh(UserInfo user) {
        String token = user.getToken();
        String account = user.getAccount();
        cache.getAndRefresh(Constants.userIdKey + account, InnerCache.day, UserInfo.class);
        cache.getAndRefresh(Constants.tokenKey + token, InnerCache.day, UserInfo.class);
    }

    /**
     * 获取登录人
     * */
    public static UserInfo getUser(RequestContext context) {
        return context.getRequestExtraInfo(RequestExtraInfo.class).getUserInfo(UserInfo.class);
    }
    
    /**
     * 获取登录用户
     * */
    public static UserInfo getUserByToken(String token) {
        return cache.getAndRefresh(Constants.tokenKey + token, InnerCache.day, UserInfo.class);
    }
    
    public static UserInfo getUserByToId(String account) {
        return cache.getAndRefresh(Constants.userIdKey + account, InnerCache.day, UserInfo.class);
    }
    
    
    /**
     * 退出登录移除缓存
     * */
    public static void removeUser(String account, String token){
        cache.delete(Constants.userIdKey + account);
        cache.delete(Constants.tokenKey + token);
    }
    
    
    /**
     * 获取所有用户
     * */
    public static List<UserInfo> getAllUser() {
        return cache.getPrefix(Constants.userIdKey, UserInfo.class);
    }
    
    
    /**
     * 默认用户 debugger
     * */
    public static UserInfo defaultUser(){
        Set<String> opers = new HashSet<>();
        opers.add(Opers.login);
        opers.add(Opers.teamNote);
        opers.add(Opers.dba);
        opers.add(Opers.java);
        
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount("bugcat");
        userInfo.setRealname("bugcat");
        userInfo.setOpers(opers);
        userInfo.setToken(userInfo.getAccount());
        userInfo.setIpAddr("127.0.0.1");
        userInfo.setLoginTime(DateUtil.ss.getDate());
        setUserInfo(userInfo);
        
        return userInfo;
    }


}
