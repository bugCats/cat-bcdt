package cc.bugcat.bcdt.logsev.websocket;


/**
 * 服务端指令
 * 
 * @author bugcat-cc
 * */
public enum ReplyMethod {
    
    /**
     * 登录成功
     * */
    loginSuccess,
    
    /**
     * 登录失败
     * */
    loginError,
    
    /**
     * 账号在其他设备登录
     * */
    loginOther,
    
    /**
     * 标准上下移动行
     * */
    reander,
    
    /**
     * 实时输出
     * */
    readAtTime,
    
    /**
     * 异步输出搜索结果
     * */
    searchInfo,
    
    
    /**
     * 异步输出搜索结果
     * */
    searchResult;

}

