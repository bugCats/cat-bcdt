package cc.bugcat.bcdt.logsev.modules.reader.files;


import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;

/**
 * 每次读取行信息；
 *
 * @author bugcat-cc
 * */
public class ReadLine {

    // 当前读取的文件
    private LogFileBean logInfo;

    // 当前读取缓存
    private byte[] bytes;
    
    // 绝对定位索引值
    private long point;

    
    public ReadLine() {}
    
    
    public LogFileBean getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(LogFileBean logInfo) {
        this.logInfo = logInfo;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }
}

