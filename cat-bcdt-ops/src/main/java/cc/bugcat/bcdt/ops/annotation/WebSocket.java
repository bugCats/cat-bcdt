package cc.bugcat.bcdt.ops.annotation;

import org.springframework.web.socket.server.HandshakeInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author bugcat-cc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebSocket {
    
    /**
     * WebSocket webSocket = AnnotationUtils.getAnnotation(handler.getClass(), WebSocket.class);
     * */
    String value();
    
    /**
     * org.springframework.web.socket.server.HandshakeInterceptor
     * */
    Class<? extends HandshakeInterceptor> interceptor() default HandshakeInterceptor.class;
}
