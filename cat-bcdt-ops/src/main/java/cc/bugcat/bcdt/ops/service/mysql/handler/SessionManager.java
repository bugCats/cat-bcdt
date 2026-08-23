package cc.bugcat.bcdt.ops.service.mysql.handler;


import cc.bugcat.bcdt.ops.model.zentao.BcDbProxy;
import cc.bugcat.bcdt.ops.service.mysql.user.UserSession;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 *
 *
 * @author bugcat-cc
 */
public class SessionManager {
    
    private static final SessionManager MANAGER = new SessionManager();
    
    private SessionManager(){}
    
    public static SessionManager getManager(){
        return MANAGER;
    }


    //授权成功之后，session保留时间 KEEP
    public final static long KEEP = 3600_000L;
    
    //数据库白名单账号
    private final Set<String> whiteListSet = new HashSet<>();
    
    //子账号
    private final ConcurrentMap<String, SubAccount> subAccountMap = new ConcurrentHashMap<>();


    
    /**
     * 注入白名单
     * */
    public void registerWhite(BcDbProxy database) {
        if ( "0".equals(database.getEnable()) || StringUtils.isBlank(database.getWhiteUser()) ) {
            return;
        }
        whiteListSet.add(database.getWhiteUser());
    }

    public boolean inWhiteList(String userCode){
        return whiteListSet.contains(userCode);
    }




    /**
     * 注入子账号
     * */
    public void register(UserSession user) {
        String userCode = user.getUserCode();
        SubAccount account = subAccountMap.get(user.getUserCode());
        if( account == null ){
            account = new SubAccount();
            subAccountMap.putIfAbsent(userCode, account);
            account = subAccountMap.get(userCode);
        }
        account.lastActive = System.currentTimeMillis();
        account.user = user;
    }
    
            
    
    /**
     * 获取子账号
     * */
    public UserSession getUserSession(String userCode, MysqlConnContext context) {
        SubAccount account = subAccountMap.get(userCode);
        if( account == null ){
            return null;
        }
        account.contextMap.put(context.getConnId(), context);
        account.lastActive = System.currentTimeMillis();
        return account.user;
    }


    /**
     * 根据前缀获取
     * */
    public UserSession getPrefix(String prefix){
        Iterator<Map.Entry<String, SubAccount>> iterator = subAccountMap.entrySet().iterator();
        while ( iterator.hasNext() ) {
            Map.Entry<String, SubAccount> entry = iterator.next();
            String key = entry.getKey();
            if( key.startsWith(prefix) ){
                SubAccount account = entry.getValue();
                return account.user;
            }
        }
        return null;
    }
    
    
    /**
     * 刷新最后活跃时间
     * */
    public void refreshActiveTime(String userCode){
        SubAccount account = subAccountMap.get(userCode);
        if( account != null ){
            account.lastActive = System.currentTimeMillis();
        }
    }

    /**
     * 判断是否失效
     * */
    public boolean isInactive(String userCode){
        SubAccount account = subAccountMap.get(userCode);
        if ( account == null ) {
            return true;
        }
        if ( (System.currentTimeMillis() - account.lastActive) > KEEP ) { //超过KEEP2没有更新
            return true;
        } else {
            return false;
        }
    }

    
    /**
     * 每隔3分钟检查一次
     * */
    public void check() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, SubAccount>> iterator = subAccountMap.entrySet().iterator();
        while ( iterator.hasNext() ) {
            Map.Entry<String, SubAccount> entry = iterator.next();
            SubAccount account = entry.getValue();

            if( now - account.lastActive > KEEP ){
                iterator.remove();
                continue;
            }
            Iterator<Map.Entry<String, MysqlConnContext>> iter = account.contextMap.entrySet().iterator();
            while ( iter.hasNext() ) {
                Map.Entry<String, MysqlConnContext> next = iter.next();
                MysqlConnContext context = next.getValue();
                if( context.isClose() ){
                    iter.remove();
                }
            }
        }
    }

    
    /**
     * 子账号：链接
     * */
    private static class SubAccount {

        /**
         * 当前子账号已存在的链接
         * */
        private final Map<String, MysqlConnContext> contextMap = new HashMap<>();

        /**
         * 最后一次授权的明细
         * */
        private UserSession user;
        
        /**
         * 当前子账号最后活跃时间
         * */
        private long lastActive;


    }
    
    
    

}
