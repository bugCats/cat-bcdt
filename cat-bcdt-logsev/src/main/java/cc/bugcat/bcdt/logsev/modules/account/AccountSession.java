package cc.bugcat.bcdt.logsev.modules.account;

import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSessionFactory;
import cc.bugcat.bcdt.logsev.modules.reader.handle.AbstractMethodHandle;
import cc.bugcat.bcdt.logsev.utils.GzipUtil;
import cc.bugcat.bcdt.logsev.websocket.ReaderSocket;
import cc.bugcat.bcdt.logsev.websocket.ReplyMethod;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;


/**
 *
 * @author bugcat-cc
 * */
public class AccountSession implements Consumer<String> {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountSession.class);
    
    private final ReaderFileSessionFactory readerFactory = new ReaderFileSessionFactory();
    
    private long heartbeat = System.currentTimeMillis();
    private WebSocketSession webskt;
    private FluxSink<WebSocketMessage> sink;

    
    protected AccountSession() { }

    /**
     * 检查websocket链接是否有效
     * */
    public boolean checkAlive() {
        long time = System.currentTimeMillis();
        if ( time - this.heartbeat > 300000L ) {
            this.readerFactory.destroy();
            this.closeWebSocket();
            return true;
        }
        return false;
    }

    /**
     * 关闭ws链接
     * */
    public void close() {
        this.readerFactory.destroy();
        this.closeWebSocket();
    }
    

    /**
     * 开启ws链接
     * */
    public void setWebSocketSession(WebSocketSession session) {
        if ( this.webskt != null ) {
            String payload = ReaderSocket.build(ReplyMethod.loginOther);
            this.sendData(payload);
            this.closeWebSocket();
        }
        this.heartbeat = System.currentTimeMillis();
        this.webskt = session;
    }

    
    public void setSinkHandle(FluxSink<WebSocketMessage> sink) {
        this.sink = sink;
    }

    
    /**
     * 接收ws客户端内容
     * */
    public Mono<Void> receive(String text) {
        
        // 心跳
        if ( "@".equals(text) ) {
            this.heartbeat = System.currentTimeMillis();
            return Mono.empty();
        }
        
        // 客户端传入指令
        return Mono.fromRunnable(() -> {
                    JsonTools param = JsonTools.parseObject(text);
                    JsonTools head = param.getJsonTools("head");
                    String body = param.getString("body");
                    String method = head.getString("method");
                    AbstractMethodHandle<?> handle = AbstractMethodHandle.getHandle(method);
                    Object vo = handle.postHandle(this, body);
                    ResultEntity<Object> resp = ResultEntity.ok(vo);
                    JsonTools result = new JsonTools();
                    result.put("head", head);
                    result.put("body", resp);
                    this.sendData(result.toJson());
                });
    }

    
    /**
     * 服务端下发消息
     * */
    public void sendData(String text) {
        if ( this.webskt != null ) {
            WebSocketMessage message = this.webskt.binaryMessage(factory -> factory.wrap(GzipUtil.encrypt(text)));
            this.sink.next(message);
        }
    }

    
    /**
     * 服务端下发消息
     * */
    @Override
    public void accept(String text) {
        this.sendData(text);
    }

    
    
    public ReaderFileSessionFactory getReaderFactory() {
        return this.readerFactory;
    }


    /**
     * 关闭ws链接
     * */
    private void closeWebSocket() {
        try {
            this.sink.complete();
            this.webskt.close(CloseStatus.NORMAL).subscribe();
        } catch ( Exception ex ) {
            logger.error("关闭ws失败：{}", BusinessException.getStackTrace(ex));
        }
    }

}

