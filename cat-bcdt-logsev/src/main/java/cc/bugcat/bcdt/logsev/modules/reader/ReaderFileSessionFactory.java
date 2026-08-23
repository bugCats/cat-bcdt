package cc.bugcat.bcdt.logsev.modules.reader;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.bcdt.logsev.config.LoggerConfig;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.ReaderInfoVi;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;


/**
 * 当前正在查看的文件会话工厂。
 * 管理会话生命周期
 *
 * @author bugcat-cc
 * */
public final class ReaderFileSessionFactory {

    private final LoggerConfig config = LoggerConfig.getConfig();
    
    
    private final Map<String, ReaderFileSession> sessionMap = new HashMap<>();

    
    /**
     * 选择文件，创建文件会话
     * */
    public ReaderFileSession openReader(ReaderInfoVi reqVi) {

        final String appId = reqVi.getAppId();
        final String logName = reqVi.getLogName();
        
        String path = this.config.getPath(appId);
        if ( !"nohup.out".equals(logName) ) {
            path = path + "/logs";
        }

        final String mapKey = this.getReaderSessionKey(appId, logName);
        ReaderFileSession session = this.sessionMap.get(mapKey);
        
        if ( session == null ) {
            File logFile = new File(path + "/" + logName);
            if ( !logFile.exists() ) {
                throw BusinessException.builder("999", Errors.ERROR, "日志文件名错误[" + logName + "]！");
            }
            try {
                LogFileBean fileBean = new LogFileBean(appId, logFile);
                session = new ReaderFileSession(fileBean);
                this.sessionMap.put(mapKey, session);
            } catch (Exception ex) {
                throw BusinessException.builder(ex);
            }
        }
        return session;
    }

    
    /**
     * 客户端关闭阅读
     * */
    public void destroy(ReaderInfoVi reqVi) {
        final String mapKey = this.getReaderSessionKey(reqVi.getAppId(), reqVi.getLogName());
        ReaderFileSession file = this.sessionMap.remove(mapKey);
        if (file != null) {
            file.destroy();
            this.sessionMap.remove(mapKey);
        }
    }

    /**
     * 用户会话关闭
     * */
    public void destroy() {
        this.sessionMap.forEach((key, value) -> value.destroy());
        this.sessionMap.clear();
    }

    
    /**
     * 如果客户端开启了实时读取，则触发输出服务端内容
     * */
    public void readerAtTime(Consumer<String> sendData) {
        this.sessionMap.forEach((key, value) -> value.readerAtTime(sendData));
    }

    
    /**
     * 获取应用id指定目录中所有日志文件
     * */
    public List<File> getLogFile(String dir) {
        File[] files = new File(dir + "/logs").listFiles();
        if (files == null || files.length == 0) {
            return new ArrayList<>(0);
        }
        List<File> logs = new ArrayList<>(files.length + 1);
        logs.addAll(Arrays.asList(files));
        File nohup = new File(dir + "/nohup.out");
        if (nohup.exists()) {
            logs.add(nohup);
        }
        return logs;
    }

    
    /**
     * 根据应用id + 文件名，生成唯一id
     * */
    private String getReaderSessionKey(String appId, String logName) {
        return appId + "@#" + logName;
    }
}

