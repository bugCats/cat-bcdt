package cc.bugcat.bcdt.ops.service.mysql.packet;



import cc.bugcat.bcdt.ops.service.mysql.handler.MysqlConnContext;
import cc.bugcat.bcdt.ops.service.mysql.logger.ByteUtil;

import java.nio.charset.Charset;

/**
 * 第一次握手，由服务端调用客户端；
 *
 *
 * @author bugcat-cc
 */
public class HandshakePacket1 {

    /**
     * 服务端返回的20位随机数
     * */
    private final byte[] random;
    
    /**
     * 密码加密规则
     * */
    private final String pwdMod;


    public HandshakePacket1(byte[] random, String pwdMod) {
        this.random = random;
        this.pwdMod = pwdMod;
    }

    
    
    /**
     * @param userPwd 数据库授权之后，子账号的密码
     * @param inputPwd tcp协议传入的密码，需要与userPwd对比
     * @param realPwd 数据库真实密码
     * */
    public byte[] checkUserPwd(byte[] userPwd, byte[] inputPwd, byte[] realPwd) throws Exception {
        
        if( "mysql_native_password".equals(pwdMod) ){

            //子账号密码
            byte[] pwd = ByteUtil.mysqlNativePassword(userPwd, random);

            //验证传入的账号密码
            if( ByteUtil.notEquals(inputPwd, pwd) ){
                throw new Exception("14000@The sub account password is incorrect!");
            }

            return ByteUtil.mysqlNativePassword(realPwd, random);
        
        } else {
            
            // caching_sha2_password
            
            throw new Exception("14000@The sub account password is incorrect!");
            
        }
    }


    public static HandshakePacket1 parsePacket(byte[] bytes){

        //字符集
        Charset charset = MysqlConnContext.ofCharset(bytes[31]);

        ByteUtil.ReaderEntry reader = ByteUtil.readToNull(bytes, 20);
        byte[] random1 = reader.getBytes();
        
        reader = ByteUtil.readToNull(bytes, 40);
        byte[] random2 = reader.getBytes();
        
        reader = ByteUtil.readToNull(bytes, reader.getEnd() + 1);
        byte[] pwdPlugin = reader.getBytes();

        byte[] random = ByteUtil.concat(random1, random2);
        String pwdMod = new String(pwdPlugin, charset);
        
        HandshakePacket1 packet = new HandshakePacket1(random, pwdMod);
        return packet;
    }


}
