package cc.bugcat.bcdt.ops.web;


/**
 * websocket下发消息事件
 *
 *
 * @author bugcat-cc
 */
public enum WebAction {

    //需要登录
    to_login,
    
    
    //自动填充数据库授权码
    db_auth,
    
    
    //浏览器弹窗提示
    web_alert,
    
    
    //系统异常数量仪表盘
    error_watch,
    
    ;
    



}

