package cc.bugcat.bcdt.ops.service.mysql.spi;

import java.io.Serial;
import java.io.Serializable;


/**
 *
 *
 * @author bugcat-cc
 */
public interface IUserAuthInfo extends Serializable {
    
    @Serial
    static final long serialVersionUID = 972245132000000011L;

    String getUserCode();
    
    String getUserPwd();

}
