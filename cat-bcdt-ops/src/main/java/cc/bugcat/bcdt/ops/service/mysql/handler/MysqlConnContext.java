package cc.bugcat.bcdt.ops.service.mysql.handler;


import cc.bugcat.bcdt.ops.model.zentao.BcDbProxy;
import cc.bugcat.bcdt.ops.service.mysql.EnableMysqlProxy;
import cc.bugcat.bcdt.ops.service.mysql.packet.HandshakePacket1;
import cc.bugcat.bcdt.ops.service.mysql.packet.HandshakePacket2;
import cc.bugcat.bcdt.ops.service.mysql.spi.ISqlLoggerService;
import cc.bugcat.bcdt.ops.service.mysql.user.UserSession;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;


/**
 * 链接上下文
 *
 *
 * @author bugcat-cc
 */
public final class MysqlConnContext {

    
    private final SessionManager manager = SessionManager.getManager();
    
    private volatile boolean closed;

    @Getter
    private final String connId = UUID.randomUUID().toString();
    
    @Getter
    private String userCode;
    
    @Getter
    private Consumer<String> writeLogs;

    @Setter
    private BcDbProxy database;

    @Setter
    private ISqlLoggerService loggerService;

    @Getter
    private Charset charset = EnableMysqlProxy.CHARSET;
    
    @Getter
    @Setter
    private int headSize = -1;

    
    @Setter
    private HandshakePacket1 handshake1;
    
    @Setter
    private HandshakePacket2 handshake2;


    @Getter
    private ServerFromHandler serverFromHandler = ServerFromHandler.first();
    
    @Getter
    private ServerToHandler serverToHandler = ServerToHandler.first();



    /**
     * 会话是否已经失效
     * */
    public void validate() throws Exception {
        if( this.closed ){ //手动关闭
            throw new Exception("15060@parse recursion of error packet!");
        }
        if( manager.isInactive(userCode) ){ // 当前时间，超过了最大保留时限
            throw new Exception("15000@Exceeded the maximum idle time!");
        }
    }
    
    
    /**
     * 执行完有效SQL后，刷新用户缓存
     * */
    public void refreshActiveTime(){
        manager.refreshActiveTime(userCode);
    }
    

    
    
    
    /**
     * 客户端使用的逻辑子账号，在登录的时候，替换成真实mysql账号。
     * */
    public byte[] doLogin() throws Exception {

        //字符集
        this.charset = ofCharset(handshake2.getCharset());
        
        String userCode = new String(handshake2.getUsername(), charset);
        
        if ( manager.inWhiteList(userCode) ) {
            return handshake2.getBytes();
        }
        

        String realusername = null;
        String realpassword = null;


        if( userCode.contains("$") ){
            String[] names = userCode.split("\\$");
            userCode = names[0];        //子账号 username@code
            realusername = names[1];    //强制指定数据库用户
        }

        UserSession user = manager.getUserSession(userCode, this);
        if ( user == null ) { //没有获取到
            throw new Exception("13000@Authorization code error!");
        }

        if ( realusername != null ){//强制指定数据库用户，要验证是否有dba权限

            //当前授权账号没有dba权限、或者指定账号未配置，不能使用指定数据库账号
            if ( !( user.isDba() && realusername.equals(database.getDbaUser()) ) ) {
                throw new Exception("13010@Sub account does not have DBA permission!");
            }

            //dba账号
            realusername = database.getDbaUser();
            realpassword = database.getDbaPwd();

        } else {
            // 没有指定数据库账号
            if ( user.isWrite() ) {
                realusername = database.getWhiteUser();
                realpassword = database.getWritePwd();
            } else {
                realusername = database.getReadUser();
                realpassword = database.getReadPwd();
            }
        }

        String userPwd = user.getUserPwd();
        
        byte[] usernames = realusername.getBytes(charset);
        
        byte[] passwords = handshake1.checkUserPwd(userPwd.getBytes(charset), handshake2.getPassword(), realpassword.getBytes(charset));
        
        byte[] loginPacket = handshake2.loginPacket()
                .username(usernames)
                .password(passwords)
                .build();
        
        this.handshake1 = null;
        this.handshake2 = null;
        this.userCode = user.getUserCode();
        this.writeLogs = user.getWriteLogs();

        return loginPacket;
    }


    /**
     * 写日志
     * */
    public void writeLog(byte[] bytes) {
        loggerService.writeLog(this, bytes); //记录日志
    }
    
    /**
     * 关闭链接
     * */
    public void close() {
        this.closed = true;
    }
    public boolean isClose(){
        return this.closed;
    }
    
    
    
    /**
     * 字符集
     * */
    public static Charset ofCharset(byte bit){
        return switch ( bit ) {
            case (byte) 0x21 -> StandardCharsets.UTF_8;
            case (byte) 0xf8 -> Charset.forName("gb18030");
            case (byte) 0x18 -> Charset.forName("gb2312");
            case (byte) 0x1c -> Charset.forName("gbk");
            case (byte) 0x08 -> StandardCharsets.ISO_8859_1;
            case (byte) 0x0b -> StandardCharsets.US_ASCII;
            default -> EnableMysqlProxy.CHARSET;
        };
    }

    
    

    public void changeFromHandler(ServerFromHandler serverFromHandler) {
        this.serverFromHandler = serverFromHandler;
    }
    public void changeToHandler(ServerToHandler serverToHandler) {
        this.serverToHandler = serverToHandler;
    }


}
