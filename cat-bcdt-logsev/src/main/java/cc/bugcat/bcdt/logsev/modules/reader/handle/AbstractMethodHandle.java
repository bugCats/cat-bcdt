package cc.bugcat.bcdt.logsev.modules.reader.handle;

import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.websocket.ReceiveMethod;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author bugcat-cc
 * */
public abstract class AbstractMethodHandle<REQ> {
    
    private static final Map<String, AbstractMethodHandle<?>> handleMap = new HashMap<>();
    
    private static final AbstractMethodHandle<Object> DEFAULT = new AbstractMethodHandle<>(ReceiveMethod.DEFAULT){
        @Override
        public Object execute(AccountSession session, Object data) {
            return null;
        }
    };
    
    private final Class<REQ> requestClass = (Class)((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    
    
    public AbstractMethodHandle(ReceiveMethod method) {
        handleMap.put(method.name(), this);
    }
    

    public static AbstractMethodHandle<?> getHandle(ReceiveMethod method) {
        return AbstractMethodHandle.getHandle(method.name());
    }
    public static AbstractMethodHandle<?> getHandle(String method) {
        return handleMap.getOrDefault(method, DEFAULT);
    }

    
    public Object postHandle(AccountSession session, String body) {
        REQ reqVi = JsonTools.parseObject(body, this.requestClass);
        return this.postHandle(session, reqVi);
    }

    
    public Object postHandle(AccountSession session, REQ reqVi) {
        try {
            return this.execute(session, reqVi);
        } catch (Exception ex) {
            return ResultEntity.fail(BusinessException.builder(ex));
        }
    }

    
    protected abstract Object execute(AccountSession var1, REQ reqVi) throws Exception;
}

