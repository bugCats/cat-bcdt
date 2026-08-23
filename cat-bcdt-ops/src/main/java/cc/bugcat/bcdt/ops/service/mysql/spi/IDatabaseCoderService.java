package cc.bugcat.bcdt.ops.service.mysql.spi;


import cc.bugcat.bcdt.ops.service.mysql.logger.LoggerEntry;

import java.util.List;


/**
 * tcp包转sql字符串
 *
 *
 * @author bugcat-cc
 */
public interface IDatabaseCoderService {

    
    List<String> coder (LoggerEntry loggerEntry);
    
}
