package cc.bugcat.bcdt.logsev.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 日志文件信息
 *
 * @author bugcat-cc
 * */
public class LogFileInfo {
    
    //日志文件在服务器上的绝对路径
    @JsonIgnore
    private String logDir;
    
    //文件名
    private String logName;
    
    //文件大小
    private String size;
    
    //加载时间
    private String date;

    
    public String getLogDir() {
        return this.logDir;
    }
    public void setLogDir(String logDir) {
        this.logDir = logDir;
    }

    public String getLogName() {
        return this.logName;
    }
    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getSize() {
        return this.size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}

