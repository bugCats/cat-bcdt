package cc.bugcat.bcdt.ops.service.mysql.handler;


import cc.bugcat.bcdt.ops.service.mysql.packet.HandshakePacket1;

/**
 * 服务端 -> 客户端；
 *
 *
 * @author bugcat-cc
 */
public class ServerFromHandler {

    
    public byte[] postHandler(MysqlConnContext context, byte[] bytes) {
        return bytes;
    }


    
    
    /**
     * 第一次请求处理类
     * */
    public static ServerFromHandler first(){
        return new FirstServerFromHandler();
    }
    
    
    /**
     * 第一次请求处理类
     * */
    private static class FirstServerFromHandler extends ServerFromHandler {

        @Override
        public byte[] postHandler(MysqlConnContext context, byte[] bytes) {
            HandshakePacket1 packet = HandshakePacket1.parsePacket(bytes);
            context.setHandshake1(packet);
            context.changeFromHandler(new ServerFromHandler()); //切换状态类
            return bytes;
        }
    }
}
