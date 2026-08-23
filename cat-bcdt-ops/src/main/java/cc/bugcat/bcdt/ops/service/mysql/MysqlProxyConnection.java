package cc.bugcat.bcdt.ops.service.mysql;

import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.bcdt.ops.service.mysql.handler.MysqlConnContext;
import cc.bugcat.bcdt.ops.service.mysql.handler.ServerFromHandler;
import cc.bugcat.bcdt.ops.service.mysql.handler.ServerToHandler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;


/**
 *
 *
 * @author bugcat-cc
 */
class MysqlProxyConnection {

    
    private MysqlConnContext context; //当前链接信息
    private NetSocket clientSocket;
    private NetSocket serverSocket;
    
    
    protected MysqlProxyConnection(MysqlConnContext context){
        this.context = context;
    }
    
    
    
    protected void setNetSocket(NetSocket clientSocket, NetSocket serverSocket) {
        this.clientSocket = clientSocket;
        this.serverSocket = serverSocket;
    }

    
    protected void doProxy() {

        //当代理与mysql服务器连接关闭时，关闭client与代理的连接
        serverSocket.closeHandler(event -> {
            closeSocket();
        });
        
        clientSocket.closeHandler(event -> {
            closeSocket();
        });
        
        
        //不管哪端的连接出现异常时，关闭两端的连接
        serverSocket.exceptionHandler(ex -> {
            EnableMysqlProxy.LOGGER.error("服务端异常：{}", ex.getMessage());
            closeSocket();
        });
        
        clientSocket.exceptionHandler(ex -> {
            EnableMysqlProxy.LOGGER.error("客户端异常：{}", ex.getMessage());
            closeSocket();
        });

        
        //当收到来自mysql目标服务器的数据包时，转发给客户端
        serverSocket.handler(buffer -> {
            try {
                byte[] bytes = buffer.getBytes();
                clientSocket.write(buffer);
                
                ServerFromHandler fromHandler = context.getServerFromHandler();
                fromHandler.postHandler(context, bytes);
            } catch ( Exception ex ) {
                closeSocket();
            }
        });
        
        //当收到来自客户端的数据包时，转发给mysql目标服务器
        clientSocket.handler(buffer -> {
            
            byte[] bytes = buffer.getBytes();
            ServerToHandler toHandler = context.getServerToHandler();
            
            byte[] sendByte = null;
            try {
                toHandler.preHandler(context, bytes);
                sendByte = toHandler.postHandler(context, bytes);
            } catch ( Exception ex ) {
                doError(toHandler, ex);
                return;
            }

            try {
                serverSocket.write(Buffer.buffer(sendByte));
            } catch ( Exception ex ) {
                doError(toHandler, ex);
                closeSocket();
            } finally {
                toHandler.onfinally(context, sendByte);  //记录日志
            }
            
        });
    }

    
    
    private void doError( ServerToHandler toHandler, Exception ex ) {
        try {
            EnableMysqlProxy.LOGGER.error("链接异常：{}", BusinessException.getStackTrace(ex));
            byte[] replyByte = toHandler.onError(ex);
            clientSocket.write(Buffer.buffer(replyByte));
        } finally {
            closeSocket();
        }
    }
    

    protected void closeSocket() {
        try { context.close(); } catch ( Exception ex ) { }
        try { clientSocket.close(); } catch ( Exception ex ) { }
        try { serverSocket.close(); } catch ( Exception ex ) { }
    }
    
    
}
