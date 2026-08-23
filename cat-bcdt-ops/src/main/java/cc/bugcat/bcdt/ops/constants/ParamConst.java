package cc.bugcat.bcdt.ops.constants;

/**
 * 参数name
 *
 * @author bugcat-cc
 */
public class ParamConst {

    
    /**
     * 异常监控，扫描数据的时间，默认3分钟
     * */
    public static final String p1001 = "p1001";
    
    /**
     * 异常监控，在规定时间内超过指定数量异常，触发警告，默认60个
     * */
    public static final String p1002 = "p1002";



    public static class ding {

        // 钉钉应用
        public final static String agentId = "ding:agent_id";
        public final static String appKey = "ding:appkey";
        public final static String appsecret = "ding:appsecret";

        // 数据授权：读账号群token
        public final static String dbAuthRead = "ding:db-auth:read";
        // 数据库授权：写账号群token
        public final static String dbAuthWrite = "ding:db-auth:write";
        // 开发人员群token
        public final static String developer = "ding:developer";
        // 管理人员群token
        public final static String admin = "ding:admin";

    }
}
