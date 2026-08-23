package cc.bugcat.bcdt.logsev.modules.reader.handle.vo;

/**
 * 开启实时输出模式
 *
 * @author bugcat-cc
 * */
public class ReaderAtTimeVo {
    
    private int readMod;
    
    private long fileSize;

    
    public int getReadMod() {
        return this.readMod;
    }
    public void setReadMod(int readMod) {
        this.readMod = readMod;
    }

    public long getFileSize() {
        return this.fileSize;
    }
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}

