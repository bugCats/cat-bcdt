package cc.bugcat.bcdt.ops.web;


import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.json.JsonTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Collections;


/**
 *
 *
 * @author bugcat-cc
 */
public class WebTopic {
    
    private final static Logger logger = LoggerFactory.getLogger(WebTopic.class);
    
    private WebTopic (){ }
    
    public static WebTopic builder(){
        return new WebTopic();
    }
    
    
    
    private Collection<WebSocketSession> webSessions;
    
    private String action; // 推送事件
    
    private String notify; // 是否重要消息
    
    private Object body;   // 推送内容
    
    
    public WebTopic webSessions(String account) {
        this.webSessions = WebSocketCtrl.userSession(account);;
        return this;
    }
    public WebTopic webSessions(WebSocketSession session) {
        this.webSessions = Collections.singletonList(session);
        return this;
    }
    
    
    public WebTopic action(WebAction action) {
        this.action = action.name();
        return this;
    }
    
    public WebTopic notify(boolean notify) {
        this.notify = notify ? "1" : "0";
        return this;
    }
    
    public WebTopic payload(Object payload) {
        this.body = payload;
        return this;
    }
    
    
    public void doSend(){
        TextMessage message = new TextMessage(this.getData());
        for ( WebSocketSession session : webSessions ) {
            if ( session.isOpen() ) {
                try {
                    session.sendMessage(message);
                } catch ( Exception ex ) {
                    logger.error(BusinessException.getStackTrace(ex));
                }
            }
        }
    }
    
    
    private String getData() {
        JsonTools data = new JsonTools();
        data.put("action", action);
        data.put("notify", notify);
        data.put("body", body);
        return data.toJson();
    }
}
