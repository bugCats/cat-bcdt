package cc.bugcat.bcdt.logsev.modules.reader.handle.vi;


/**
 * 搜索模式下，查看指定结果的前后多少行内容
 *
 * @author bugcat-cc
 * */
public class LimiterLineVi extends ReaderInfoVi {
    
    //指定结果的行数
    private long lineStart;
    
    //向前多少行
    private int before;
    
    //先后多少行
    private int after;

    
    
    public long getLineStart() {
        return this.lineStart;
    }
    public void setLineStart(long lineStart) {
        this.lineStart = lineStart;
    }

    public int getBefore() {
        return this.before;
    }
    public void setBefore(int before) {
        this.before = before;
    }

    public int getAfter() {
        return this.after;
    }
    public void setAfter(int after) {
        this.after = after;
    }
}

