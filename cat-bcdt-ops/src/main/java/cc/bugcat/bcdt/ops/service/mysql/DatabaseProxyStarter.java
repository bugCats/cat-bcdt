package cc.bugcat.bcdt.ops.service.mysql;


import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.spi.IWebApplication;
import cc.bugcat.bcdt.ops.model.zentao.BcDbProxy;
import cc.bugcat.bcdt.ops.service.inter.BcDbProxyService;
import cc.bugcat.bcdt.ops.service.mysql.handler.MysqlConnContext;
import cc.bugcat.bcdt.ops.service.mysql.spi.IDatabaseProxy;
import cc.bugcat.bcdt.ops.service.mysql.spi.ISqlLoggerService;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.net.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *
 *
 * @author bugcat-cc
 */
public class DatabaseProxyStarter implements IWebApplication, IDatabaseProxy {

    
    private final static Logger logger = EnableMysqlProxy.LOGGER;

    
    private final Map<Long, NetServer> serverMap = new ConcurrentHashMap<>();
    
    @Autowired
    private BcDbProxyService dbProxyService;
    
    @Autowired
    private ISqlLoggerService loggerService;
    
    private MysqlProxyServerVertical proxyServer;


    @Override
    public void onStart(ApplicationContext context) {
        InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.INSTANCE);
        proxyServer = new MysqlProxyServerVertical();
        VertxOptions options = new VertxOptions();
        options.setWorkerPoolSize(20);
        Vertx vertx = Vertx.vertx(options);
        vertx.deployVerticle(proxyServer);
    }


    
    @Override
    public void proxyStatus(BcDbProxy database) {
        if ( "1".equals(database.getEnable()) ) {
            serverMap.computeIfAbsent(database.getId(), (id) -> proxyServer.createProxy(database));
        } else {
            NetServer server = serverMap.remove(database.getId());
            if (server != null) {
                server.close();
            }
        }
    }

    
    private class MysqlProxyServerVertical extends AbstractVerticle {
        
        private final NetServerOptions serverOptions = new NetServerOptions();
        private final NetClientOptions clientOptions = new NetClientOptions();

        @Override
        public void start() throws Exception {
            
            final int SIZE = 8 * 1024 *1024; //缓存区8M
            
            serverOptions.setIdleTimeout(3630);         //超过1小时无链接，关闭
            serverOptions.setReceiveBufferSize(SIZE);
            serverOptions.setSendBufferSize(SIZE);

            clientOptions.setIdleTimeout(3630); 
            clientOptions.setConnectTimeout(10000);
            clientOptions.setSendBufferSize(SIZE);
            clientOptions.setReceiveBufferSize(SIZE);
            
            List<BcDbProxy> list = dbProxyService.listByAble();
            
            for ( BcDbProxy database : list) {
                serverMap.computeIfAbsent(database.getId(), (id) -> createProxy(database));
            }
        }
        
        
        public NetServer createProxy(BcDbProxy database) {

            if( "0".equals(database.getEnable()) ){
                return null;
            }
            
            if ( StringUtils.isAnyBlank(database.getWhiteUser(), database.getWritePwd()) ) {
                database.setWhiteUser(database.getDbaUser());
                database.setWritePwd(database.getDbaPwd());
            }
            if ( StringUtils.isAnyBlank(database.getReadUser(), database.getReadPwd()) ) {
                database.setReadUser(database.getWhiteUser());
                database.setReadPwd(database.getWritePwd());
            }


            String[] ipAddr = null;
            String hostAddr = database.getHostAddr();
            if( hostAddr.contains(":") ){
                ipAddr = hostAddr.split(":");
            } else {
                ipAddr = new String[] {hostAddr, "3306"};
            }

            //远程mysql服务器  ip:port
            SocketAddress remote = SocketAddress.inetSocketAddress(Integer.parseInt(ipAddr[1]), ipAddr[0]);

            //本地代理端口
            SocketAddress local = SocketAddress.inetSocketAddress(database.getLocalPort(), "127.0.0.1");

            //创建代理服务器
            NetServer netServer = vertx.createNetServer(serverOptions);
            
            // 代理接收到客户端后执行回调
            netServer.connectHandler(serverSocket -> {
                
                //创建连接mysql客户端
                final NetClient netClient = vertx.createNetClient();
                
                //与目标mysql服务器成功连接连接之后，创造一个MysqlProxyConnection对象,并执行代理方法
                final MysqlConnContext context = new MysqlConnContext();
                context.setLoggerService(loggerService);
                context.setDatabase(database);

                final MysqlProxyConnection connection = new MysqlProxyConnection(context);

                
                // 代理接收到客户端请求之后，马上链接真实数据库
                netClient.connect(remote, result -> {

                    //响应来自客户端的连接请求，成功之后，在建立一个与目标mysql服务器的连接
                    if ( result.succeeded() ) {
                        NetSocket clientSocket = result.result();
                        connection.setNetSocket(serverSocket, clientSocket);
                        connection.doProxy();
                    } else {
                        try { context.close(); } catch ( Exception ex ) { }
                        try { serverSocket.close(); } catch ( Exception ex ) { }
                        logger.error("链接目标服务器异常！", result.cause());
                    }
                });
                
            });

            // 代理启动后回调
            netServer.listen(local, result -> {
                final String proxyName = database.getName();
                if ( result.succeeded() ) {
                    logger.warn("{} 代理服务启动成功！{}", proxyName, local.toString());
                } else {
                    logger.error("{} 代理服务启动失败！{}", proxyName, BusinessException.getStackTrace(result.cause()));
                }
            });
            
            return netServer;
        }
    }
    
    
}
