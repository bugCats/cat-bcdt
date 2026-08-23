package cc.bugcat.bcdt.ops.openapi.listeners.events;

import cc.bugcat.acore.framework.utils.SpringContextUtil;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


/**
 *
 *
 * @author bugcat-cc
 */
public class DbProxyStatusEvent extends ApplicationEvent {

    @Getter
    private final Long id;

    public DbProxyStatusEvent(Builder builder) {
        super(builder.source);
        this.id = builder.id;
    }
    
    
    public static Builder with(Object source) {
        return new Builder(source);   
    }
    
    
    public static class Builder {
        private Object source;
        private Long id;

        private Builder(Object source) {
            this.source = source;
        }

        public Builder id ( Long id ) {
            this.id = id;
            return this;
        }
        public void publishEvent() {
            DbProxyStatusEvent event = new DbProxyStatusEvent(this);
            SpringContextUtil.publishEvent(event);
        }
    }
}
