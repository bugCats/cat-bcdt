package cc.bugcat.bcdt.logsev.modules.reader.handle.vo;


/**
 * 当前读取的文件信息
 *
 * @author bugcat-cc
 * */
public class ReaderFileInfo {
    
    //文件名
    private String logName;
    
    //读取时间
    private String openTime;
    
    //读取时文件大小
    private String openSize;

    
    
    public String getLogName() {
        return this.logName;
    }
    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getOpenTime() {
        return this.openTime;
    }
    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getOpenSize() {
        return this.openSize;
    }
    public void setOpenSize(String openSize) {
        this.openSize = openSize;
    }
}

