package cc.bugcat.bcdt.ops.service.mysql.spi;

import cc.bugcat.bcdt.ops.model.zentao.BcDbProxy;


/**
 *
 *
 * @author bugcat-cc
 */
public interface IDatabaseProxy {
    
    /**
     * 启用、禁用
     * */
    default void proxyStatus(BcDbProxy database) {
        
    }
    
}
