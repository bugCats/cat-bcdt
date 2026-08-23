package cc.bugcat.bcdt.ops.service.mysql.spi;


import cc.bugcat.bcdt.ops.service.mysql.handler.MysqlConnContext;

/**
 * 打印sql日志
 *
 *
 * @author bugcat-cc
 */
public interface ISqlLoggerService {

    
    void writeLog(MysqlConnContext context, byte[] bytes);

}
