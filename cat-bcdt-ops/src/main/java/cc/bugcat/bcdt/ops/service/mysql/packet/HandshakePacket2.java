package cc.bugcat.bcdt.ops.service.mysql.packet;


import cc.bugcat.bcdt.ops.service.mysql.logger.ByteUtil;

/**
 * 第二次握手，由客户端调用服务端；
 * 替换真实的账号密码
 *
 *
 * @author bugcat-cc
 */
public class HandshakePacket2 {

    public static HandshakePacket2 parsePacket(byte[] bytes){
        return new Builder().build(bytes);
    }

    
    
    
    /**
     * 解析
     * */
    private static class Builder {
        private byte[] bytes;       //原始报文
        private int offset = 0;     //下一个读取的开始位置
        private byte[] packetSize;
        private byte[] packetNo;
        private byte[] capabilityFlags;
        private byte[] maxPacketSize;
        private byte[] characterSet;
        private byte[] padding;
        private byte[] username;
        private byte[] usernameStop;
        private byte[] passwordSize;
        private byte[] password;
        private byte[] other;

        public HandshakePacket2 build(byte[] bytes) {
            
            this.bytes = bytes;
            
            this.packetSize = readByte(3);
            
            this.packetNo = readByte(1);

            this.capabilityFlags = readByte(4);

            this.maxPacketSize = readByte(4);

            this.characterSet = readByte(1);

            ByteUtil.ReaderEntry padding = ByteUtil.readNull(bytes, offset);
            this.padding = padding.getBytes();
            this.offset = padding.getEnd();

            ByteUtil.ReaderEntry username = ByteUtil.readToNull(bytes, offset);
            this.username = username.getBytes();
            this.offset = username.getEnd(); //末尾有个0x00停止符

            this.usernameStop = readByte(1);
            
            this.passwordSize = readByte(1); 

            int pwdLen = ByteUtil.bytesToInt(passwordSize, 0, 1);
            this.password = readByte(pwdLen);

            int length = this.bytes.length - this.offset;
            this.other = readByte(length);

            return new HandshakePacket2(this);
        }
        
        /**
         * 读取length长度字节
         * */
        private byte[] readByte(int length){
            byte[] read = new byte[length];
            System.arraycopy(bytes, offset, read, 0, length);
            this.offset += length;
            return read;
        }
    }


    /**
     * 原始报文
     * */
    private final byte[] bytes;
    
    /**
     * 包长度
     * */
    private final byte[] packetSize; 
    
    /**
     * 包序列号
     * */
    private final byte[] packetNo;
    
    /**
     * 客户端支持的属性
     * */
    private final byte[] capabilityFlags;
    
    /**
     * 报文的最大字节数
     * */
    private final byte[] maxPacketSize;

    /**
     * 字符集
     * */
    private final byte[] characterSet;

    /**
     * 填充0x00
     * */
    private final byte[] padding;

    /**
     * 数据库账号
     * */
    private final byte[] username;
    
    /**
     * 用户名停止位
     * */
    private final byte[] usernameStop;
    
    /**
     * 密码长度
     * */
    private final byte[] passwordSize;
    
    /**
     * 密码
     * */
    private final byte[] password;
    
    /**
     * 其他数据
     * */
    private final byte[] other;

    
    private HandshakePacket2(Builder builder) {
        this.bytes = builder.bytes;
        this.packetSize = builder.packetSize;
        this.packetNo = builder.packetNo;
        this.capabilityFlags = builder.capabilityFlags;
        this.maxPacketSize = builder.maxPacketSize;
        this.characterSet = builder.characterSet;
        this.padding = builder.padding;
        this.username = builder.username;
        this.usernameStop = builder.usernameStop;
        this.passwordSize = builder.passwordSize;
        this.password = builder.password;
        this.other = builder.other;
    }

    
    public byte[] getBytes() {
        return bytes;
    }

    public byte getCharset(){
        return characterSet[0];
    }

    public byte[] getUsername() {
        return username;
    }

    public byte[] getPassword() {
        return password;
    }




    public LoginPacket loginPacket(){
        return new LoginPacket(this);
    }
    

    /**
     * 创建新的登录报文
     * */
    public static class LoginPacket {
        private byte[] packetSize;
        private byte[] packetNo;
        private byte[] capabilityFlags;
        private byte[] maxPacketSize;
        private byte[] characterSet;
        private byte[] padding;
        private byte[] username;
        private byte[] usernameStop;
        private byte[] passwordSize;
        private byte[] password;
        private byte[] other;

        private LoginPacket(HandshakePacket2 packet){
            this.packetSize = packet.packetSize;
            this.packetNo = packet.packetNo;
            this.capabilityFlags = packet.capabilityFlags;
            this.maxPacketSize = packet.maxPacketSize;
            this.characterSet = packet.characterSet;
            this.padding = packet.padding;
            this.username = packet.username;
            this.usernameStop = packet.usernameStop;
            this.passwordSize = packet.passwordSize;
            this.password = packet.password;
            this.other = packet.other;
        }

        public LoginPacket username(byte[] username) {
            this.username = username;
            return this;
        }

        public LoginPacket password(byte[] password) {
            this.password = password;
            return this;
        }


        public byte[] build(){
            byte[] bytes = ByteUtil.concat(
                    packetSize,
                    packetNo,
                    capabilityFlags,
                    maxPacketSize,
                    characterSet,
                    padding,
                    username,
                    usernameStop,
                    passwordSize,
                    password,
                    other
            );
            byte[] length = ByteUtil.intToBytes(bytes.length - 4, 3);//新报文长度
            System.arraycopy(length, 0, bytes, 0, 3);
            return bytes;
        }
    }


}
