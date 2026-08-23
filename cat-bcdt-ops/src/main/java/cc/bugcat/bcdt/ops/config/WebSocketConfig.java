package cc.bugcat.bcdt.ops.config;

import cc.bugcat.bcdt.ops.annotation.WebSocket;
import cc.bugcat.bcdt.ops.moduls.WebSocketHandlerWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;


/**
 * 注册websocket
 * @author bugcat-cc
 */
@Component
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ApplicationContext applicationContext;
    

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        Map<String, Object> webSocketMap = applicationContext.getBeansWithAnnotation(WebSocket.class);
        webSocketMap.forEach((name, bean) -> {
            if (!(bean instanceof WebSocketHandler)) {
                throw new RuntimeException( String.format("Controller [%s] doesn't implement WebSocketHandlerWrap interface.", bean.getClass().getName()));
            }
            WebSocket webSocket = AnnotationUtils.findAnnotation(bean.getClass(), WebSocket.class);
            WebSocketHandlerWrap handlerWrap = new WebSocketHandlerWrap((WebSocketHandler) bean);
            //注册 handler,这里的 url 要与页面的 url 一致
            WebSocketHandlerRegistration registration = registry.addHandler(handlerWrap, webSocket.value());
            registration.setAllowedOrigins("*"); // 允许请求的域名
            Class<? extends HandshakeInterceptor> interceptorClass = webSocket.interceptor();
            if( !HandshakeInterceptor.class.equals(interceptorClass) ){
                HandshakeInterceptor interceptor = applicationContext.getBean(interceptorClass);
                registration.addInterceptors(interceptor);  //添加握手拦截
            }
        });

    }
    
}
