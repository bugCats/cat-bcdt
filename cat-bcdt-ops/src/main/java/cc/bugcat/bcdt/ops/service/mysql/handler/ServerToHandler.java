package cc.bugcat.bcdt.ops.service.mysql.handler;


import cc.bugcat.bcdt.ops.service.mysql.logger.ByteUtil;
import cc.bugcat.bcdt.ops.service.mysql.packet.ErrorPacket;
import cc.bugcat.bcdt.ops.service.mysql.packet.HandshakePacket2;

/**
 * 客户端 -> 服务器；
 *
 *
 * @author bugcat-cc
 */
public class ServerToHandler {
    
    
    /**
     * 预处理验证，链接是否超时
     * */
    public void preHandler(MysqlConnContext context, byte[] bytes) throws Exception {
        context.validate();
    }
    
    /**
     * 请求服务端前处理
     * */
    public byte[] postHandler(MysqlConnContext context, byte[] bytes) {
        return bytes;
    }
    
    /**
     * 请求完后处理，记录日志
     * */
    public void onfinally(MysqlConnContext context, byte[] bytes) {
        context.writeLog(bytes);
    }

    /**
     * 当异常后处理
     * */
    public byte[] onError(Exception ex){
        ErrorPacket errorPacket = ErrorPacket.builder()
                .errorCode(ErrorPacket.ER_ABORTING_CONNECTION)
                .errorMessage(ex.getMessage())
                .build();
        return errorPacket.toBytes();
    }


    


    public static ServerToHandler first(){
        return new FirstServerToHandler();
    }
    
    
    
    private static class FirstServerToHandler extends ServerToHandler {
        
        private byte[] result;
        
        @Override
        public void preHandler(MysqlConnContext context, byte[] bytes) throws Exception {
            HandshakePacket2 packet = HandshakePacket2.parsePacket(bytes);
            context.setHandshake2(packet);
            context.changeToHandler(new SecondServerToHandler()); //切换状态类
            this.result = context.doLogin();
        }

        @Override
        public byte[] postHandler(MysqlConnContext context, byte[] bytes) {
            return result;
        }

        @Override
        public void onfinally(MysqlConnContext context, byte[] bytes) {
            
        }

        @Override
        public byte[] onError(Exception ex) {
            ErrorPacket errorPacket = ErrorPacket.builder()
                    .errorCode(ErrorPacket.ER_ACCESS_DENIED_ERROR)
                    .errorMessage(ex.getMessage())
                    .build();
            return errorPacket.toBytes();
        }
    }
    
    
    
    
    
    
    
    /**
     * 客户端第二次请求；只获取包头信息
     * */
    private static class SecondServerToHandler extends ServerToHandler {
        
        /**
         * 只获取包头长度，用于判断是桌面工具链接，还是应用链接
         * */
        public void preHandler(MysqlConnContext context, byte[] bytes) throws Exception {
            int headSize = bytes.length - ByteUtil.bytesToInt(bytes, 0, 3);
            context.setHeadSize(headSize);
            context.validate();
            context.changeToHandler(new ServerToHandler()); //切换状态类
        }
        
    }
    
}
