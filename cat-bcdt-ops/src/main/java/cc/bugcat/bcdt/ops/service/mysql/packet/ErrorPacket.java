package cc.bugcat.bcdt.ops.service.mysql.packet;


import cc.bugcat.bcdt.ops.service.mysql.logger.ByteUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 代理过程中出现的异常包：
 * 
 * 1、超时链接被关闭
 * 2、账号密码错误
 *
 *
 * @author bugcat-cc
 */
public class ErrorPacket {

    public static Builder builder(){
        return new Builder();
    }


    public static class Builder {
        
        private byte[] packetSize;
        private byte[] packetNo;
        private byte[] sqlCmd;
        private byte[] errorCode;
        private byte[] sqlStateMarker;
        private byte[] sqlState;
        private byte[] errorMessage;
        
        public Builder errorCode(byte[] errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        /**
         * 不要传入中文，会出现乱码！
         * */
        public Builder errorMessage(String errMsg) {
            this.errorMessage = StringUtils.defaultString(errMsg, "13020@user is null！").getBytes();
            return this;
        }
        
        public ErrorPacket build(){
            this.packetSize =  new byte[3];
            this.packetNo = new byte[] {0x02};
            this.sqlCmd = new byte[] {(byte) 0xff};
            this.sqlStateMarker = new byte[] {0x23};
            this.sqlState  = new byte[] {0x32, 0x38, 0x30, 0x30, 0x30 };
            return new ErrorPacket(this);
        }
    }

    
    /**
     * 密码错误
     * */
    public final static byte[] ER_ACCESS_DENIED_ERROR = new byte[] {(byte) 0x15, (byte) 0x04};
    /**
     * 超时
     * */
    public final static byte[] ER_ABORTING_CONNECTION = new byte[] {(byte) 0x80, (byte) 0x04};

    
    /**
     * 包长度
     * :3
     * */
    private final byte[] packetSize;

    /**
     * 包序列号
     * :1
     * */
    private final byte[] packetNo;

    /**
     * mysql指令
     * :1
     * */
    private final byte[] sqlCmd;

    /**
     * 错误编码
     * :2
     * */
    private final byte[] errorCode;

    /**
     * 固定分隔符
     * :1
     * */
    private final byte[] sqlStateMarker;

    /**
     * 服务端状态
     * :5
     * */
    private final byte[] sqlState;

    /**
     * 错误说明
     * */
    private final byte[] errorMessage;


    private ErrorPacket(Builder builder){
        this.packetSize = builder.packetSize;
        this.packetNo = builder.packetNo;
        this.sqlCmd = builder.sqlCmd;
        this.errorCode = builder.errorCode;
        this.sqlStateMarker = builder.sqlStateMarker;
        this.sqlState = builder.sqlState;
        this.errorMessage = builder.errorMessage;
    }

    
    public byte[] toBytes(){
        byte[] bytes = ByteUtil.concat(
                packetSize,
                packetNo,
                sqlCmd,
                errorCode,
                sqlStateMarker,
                sqlState,
                errorMessage
        );
        byte[] length = ByteUtil.intToBytes(bytes.length - 4, 3);//新报文长度
        System.arraycopy(length, 0, bytes, 0, 3);
        return bytes;
    }
}
