package cc.bugcat.bcdt.logsev.modules.reader.files;


/**
 * 每次读取行信息；
 * 随机指定point，向前、先后找到换行符，读取当前位置整行内容
 *
 * @author bugcat-cc
 * */
public class ReadLineInfo {
    
    /**
     * 文件大小
     * */
    private final long fileSize;
    
    /**
     * 读取缓存的大小
     * */
    private long bufferSize;
    
    /**
     * 当前指定的绝对索引
     * */
    private long point;
    
    /**
     * 行开始索引
     * */
    private long lineStart;
    
    /**
     * 行结束索引
     * */
    private long lineEnd;
    
    
    /**
     * 读取内容
     * */
    private String text;

    
    
    public ReadLineInfo() {
        this.fileSize = 0L;
    }

    public ReadLineInfo(long fileSize) {
        this.fileSize = fileSize;
    }

    
    
    
    protected long getFileSize() {
        return this.fileSize;
    }
    
    protected long getBufferSize() {
        return this.bufferSize;
    }
    protected void setBufferSize(long bufferSize) {
        this.bufferSize = bufferSize;
    }

    protected long getPoint() {
        return this.point;
    }
    protected void setPoint(long point) {
        this.point = point;
    }

    public long getLineStart() {
        return this.lineStart;
    }
    public void setLineStart(long lineStart) {
        this.lineStart = lineStart;
    }

    public long getLineEnd() {
        return this.lineEnd;
    }
    public void setLineEnd(long lineEnd) {
        this.lineEnd = lineEnd;
    }

    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
}

