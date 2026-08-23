package cc.bugcat.bcdt.logsev.modules.reader.handle.vo;

/**
 * 读取的行信息
 *
 * @author bugcat-cc
 * */
public class LineInfo {
    
    //行开始索引
    private long lineStart;
    
    //行结束索引
    private long lineEnd;
    
    //行内容
    private String text;

    
    
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

