package cc.bugcat.bcdt.ops.openapi.listeners;


import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.bcdt.ops.model.zentao.BcDbProxy;
import cc.bugcat.bcdt.ops.openapi.listeners.events.DbProxyStatusEvent;
import cc.bugcat.bcdt.ops.service.inter.BcDbProxyService;
import cc.bugcat.bcdt.ops.service.mysql.spi.IDatabaseProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 *
 *
 * @author bugcat-cc
 */
@Component
public class ApplicationListeners {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationListeners.class);


    /**
     * 数据库代理
     * */
    @Component
    public static class DbProxyStatusListener implements ApplicationListener<DbProxyStatusEvent> {

        @Autowired
        private IDatabaseProxy databaseProxy;
        @Autowired
        private BcDbProxyService dbProxyService;
        
        @Override
        public void onApplicationEvent(DbProxyStatusEvent event) {
            try {
                BcDbProxy dbProxy = dbProxyService.getById(event.getId());
                databaseProxy.proxyStatus(dbProxy);
            } catch ( Exception ex ) {
                logger.error("DbProxyEnableListener异常：{}", BusinessException.getStackTrace(ex));
            }
        }
    }

}
