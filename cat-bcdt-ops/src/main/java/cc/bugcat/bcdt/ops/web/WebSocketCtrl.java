package cc.bugcat.bcdt.ops.web;


import cc.bugcat.bcdt.ops.annotation.WebSocket;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 *
 * @author bugcat-cc
 */
@Component
@WebSocket(value = "/websocket", interceptor = WebSocketCtrl.Interceptor.class)
public class WebSocketCtrl extends TextWebSocketHandler {

    private static final String USER_INFO_KEY = "userInfo";
    
    private static ConcurrentMap<String, ConcurrentMap<String, WebSocketSession>> sessionMap = new ConcurrentHashMap<>();
    
    
    public static Set<String> getOnline(){
        return sessionMap.keySet();
    }
    
    
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        UserInfo userInfo = getUserInfo(session);
        if( StringUtils.isNotBlank(userInfo.getToken()) ) {
            String account = userInfo.getAccount();
            sessionMap.putIfAbsent(account, new ConcurrentHashMap<>());
            ConcurrentMap<String, WebSocketSession> userSession = sessionMap.get(account);
            userSession.put(session.getId(), session);
        } else {
            WebTopic.builder()
                    .webSessions(session).action(WebAction.to_login)
                    .payload("请登录")
                    .doSend();
            session.close(CloseStatus.NORMAL);
        }
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        UserInfo userInfo = getUserInfo(session);
        String payload = message.getPayload();
        if ( "@".equals(payload) ) {
            UserUtil.getUserByToId(userInfo.getAccount());
        }
    }
    
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        this.afterConnectionClosed(session, null);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UserInfo userInfo = getUserInfo(session);
        if ( StringUtils.isNotBlank(userInfo.getAccount()) ) {
            String account = userInfo.getAccount();
            ConcurrentMap<String, WebSocketSession> userSession = sessionMap.get(account);
            if ( userSession != null ) {
                userSession.remove(session.getId());
                if ( userSession.isEmpty() ) {
                    sessionMap.remove(account);
                }
            }
        }
    }


    
    
    private UserInfo getUserInfo(WebSocketSession session){
        Map<String, Object> attributes = session.getAttributes();
        UserInfo userInfo = (UserInfo) attributes.get(USER_INFO_KEY);
        return userInfo;
    }
    
    
    
    /**
     * 如果是 ---，表示全部；#xxx表示包含xxx权限的人
     * */
    protected static Collection<WebSocketSession> userSession(String account){
        if( account.startsWith("#")){
            Collection<WebSocketSession> users = new ArrayList<>();
            sessionMap.forEach((user, map) -> {
                UserInfo info = UserUtil.getUserByToId(user);
                if( info != null ){
                    if ( Opers.hasOper(info, account)) {
                        users.addAll(map.values());
                    }
                }
            });
            return users;
        } else if ( "---".equals(account) ){
            Collection<WebSocketSession> users = new ArrayList<>();
            sessionMap.forEach((user, map) -> {
                users.addAll(map.values());
            });
            return users;
        }
        
        ConcurrentMap<String, WebSocketSession> userSession = sessionMap.get(account);
        if( userSession != null ){
            return userSession.values();
        } else {
            return new ArrayList<>(0);
        }
    }
    
    
    
    
    /**
     * websocket拦截器
     * */
    @Component
    public static class Interceptor implements HandshakeInterceptor {
        
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            if( request instanceof ServletServerHttpRequest httpRequest){
                HttpServletRequest hreq = httpRequest.getServletRequest();
                String token = hreq.getParameter("token");
                UserInfo userInfo = UserUtil.getUserByToken(token);
                if ( userInfo == null ){
                    userInfo = new UserInfo();
                    userInfo.setAccount("");
                }
                attributes.put(USER_INFO_KEY, userInfo);
                return true;
            }
            return false;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

        }
    }

}
