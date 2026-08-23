package cc.bugcat.bcdt.logsev.websocket;


/**
 * 客户端指令
 * @author bugcat-cc
 * */
public enum ReceiveMethod {
    
    /**
     * 打开文件
     * */
    openFile,
    
    /**
     * 跳转到文件开头
     * */
    gotoFileHome,
    
    /**
     * 跳转到文件末尾
     * */
    gotoFileEnd,
    
    /**
     * 上下移动行
     * */
    moveLine,
    
    /**
     * 修改阅读模式
     * */
    readMod,
    
    /**
     * 停止阅读
     * */
    readStop,
    
    /**
     * 搜索模式，点击搜索结果，显示上下n行
     * */
    limiterLine,
    
    /**
     * 搜索模式，点击搜索结果，显示并上下翻页
     * */
    limiterLineMore,
    
    
    /**
     * 默认指令
     * */
    DEFAULT;

}

