package cc.bugcat.bcdt.ops.moduls;

import cc.bugcat.acore.framework.exception.BusinessException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketHandlerWrap implements WebSocketHandler {

    private final WebSocketHandler socketHandler;
    
    public WebSocketHandlerWrap(WebSocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws BusinessException {
        try {
            socketHandler.afterConnectionEstablished(session);
        } catch ( Exception ex ) {
            throw BusinessException.builder(ex);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws BusinessException {
        try {
            socketHandler.handleMessage(session, message);
        } catch ( Exception ex ) {
            throw BusinessException.builder(ex);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws BusinessException {
        try {
            socketHandler.handleTransportError(session, exception);
        } catch ( Exception ex ) {
            throw BusinessException.builder(ex);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws BusinessException {
        try {
            socketHandler.afterConnectionClosed(session, status);
        } catch ( Exception ex ) {
            throw BusinessException.builder(ex);
        }
    }

    @Override
    public boolean supportsPartialMessages() throws BusinessException {
        try {
            return socketHandler.supportsPartialMessages();
        } catch ( Exception ex ) {
            throw BusinessException.builder(ex);
        }
    }
}
