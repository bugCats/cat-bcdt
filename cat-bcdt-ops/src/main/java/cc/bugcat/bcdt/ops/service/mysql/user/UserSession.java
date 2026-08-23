package cc.bugcat.bcdt.ops.service.mysql.user;

import cc.bugcat.bcdt.ops.service.mysql.EnableMysqlProxy;
import cc.bugcat.bcdt.ops.service.mysql.spi.IUserAuthInfo;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * 连接会话；只保留同一个人同一种类型的最后一次授权
 *
 *
 * @author bugcat-cc
 */
public class UserSession implements IUserAuthInfo {
    
    
    private final String userCode;  //子账号 username@code
    private final String userPwd;   //子账号密码
    
    
    private final File logFile;     //日志路径
    private final String write;     //是否只读账号
    private final Boolean dba;      //是否为dba
    
    
    protected UserSession(Builder builder) {
        this.userCode = builder.userCode;
        this.userPwd = builder.userPwd;
        this.logFile = builder.logFile;
        this.write = builder.write;
        this.dba = builder.dba == null ? Boolean.FALSE : builder.dba;
    }

    
    public Consumer<String> getWriteLogs(){
        Consumer<String> writeLogs = new WriteLogsConsumer(logFile);
        return writeLogs;
    }
    
    
    
    @Override
    public String getUserCode() {
        return userCode;
    }
    @Override
    public String getUserPwd() {
        return userPwd;
    }
    
    public boolean isDba(){
        return dba;
    }
    public boolean isWrite() {
        return "1".equals(write);
    }

    
    
    /**
     * 打印日志
     * */
    private static class WriteLogsConsumer implements Consumer<String> {
        private final File logFile;
        private WriteLogsConsumer(File logFile) {
            this.logFile = logFile;
        }
        @Override
        public void accept(String content) {
            try {
                FileUtils.write(logFile, content, EnableMysqlProxy.CHARSET, true);
            } catch ( Exception ex ) {
                EnableMysqlProxy.LOGGER.error("写入日志异常：{}", ex.getMessage());
            }
        }
    }

    
    
    
    /*************************************************************************/
    
    
    public static Builder builder(){
        return new Builder();
    }



    public static class Builder {

        private String userCode;  //子账号 username@code
        private String userPwd;   //子账号密码
        private Boolean dba;      //是否为dba
        private String write;     //是否只读
        private File logFile;     //日志路径
        
        private Function<Builder, UserSession> create = builder -> new UserSession(builder);
        
        private Builder(){}

        public Builder userCode(String userCode) {
            this.userCode = userCode;
            return this;
        }
        public Builder userPwd(String userPwd) {
            this.userPwd = userPwd;
            return this;
        }
        public Builder write(String write) {
            this.write = write;
            return this;
        }
        public Builder dba(boolean dba) {
            this.dba = dba;
            return this;
        }

        public Builder logFile(File logFile) {
            this.logFile = logFile;
            return this;
        }
        
        public Builder whiteList(){
            this.create = builder -> new WhiteListUserSession(builder);
            return this;
        }
        
        public UserSession build(){
            UserSession session = create.apply(this);
            return session;
        }
    }
    

}
