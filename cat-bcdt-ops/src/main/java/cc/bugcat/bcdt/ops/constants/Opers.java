package cc.bugcat.bcdt.ops.constants;


import cc.bugcat.acore.framework.spi.IUserInfo;

import java.util.Set;


/**
 * api接口权限编号
 * 
 * @author bugcat-cc
 */
public class Opers {

    /**
     * 判断是否包含权限
     * */
    public static boolean hasOper(IUserInfo userInfo, String need){
        return hasOper(userInfo.getOpers(), need);
    }
    public static boolean hasOper(Set<String> opers, String need){
        return opers.contains(need);
    }


    public static boolean noOper(IUserInfo userInfo, String need){
        return !hasOper(userInfo, need);
    }
    public static boolean noOper(Set<String> opers, String need){
        return !hasOper(opers, need);
    }
    
    
    
    
    /**
     * 登录
     * */
    public static final String login = "login";
    
    
    /**
     * 查询日报的数据权限，全部
     * */
    public static final String teamNote = "#1103";

    /**
     * mysql dba 拥有最高权限
     * */
    public static final String dba = "#1201";
    
    /**
     * sonarAdmin管理员
     * */
    public static final String sonarAdmin = "#1301";

    /**
     * 后台组，发送接收错误报告、系统异常提醒
     * */
    public static final String java = "#3100";

}
