package cc.bugcat.bcdt.logsev.modules.reader.handle.vi;


/**
 * 上下移动行
 *
 * @author bugcat-cc
 * */
public class MoveLineVi extends ReaderInfoVi {
    
    //客户端窗口第一行的开始索引
    private long pageStart;
    
    //客户端窗口最后一行的结束索引
    private long pageEnd;
    
    //返回内容的添加模式：tail 追加到末尾；head 追加到开头
    private String append;
    
    //是否向下移动
    private boolean next;
    
    //加载数据范围：line 行；page 整个客户端窗口
    private String range;

    
    
    public long getPageStart() {
        return this.pageStart;
    }

    public void setPageStart(long pageStart) {
        this.pageStart = pageStart;
    }

    public long getPageEnd() {
        return this.pageEnd;
    }

    public void setPageEnd(long pageEnd) {
        this.pageEnd = pageEnd;
    }

    public String getAppend() {
        return this.append;
    }

    public void setAppend(String append) {
        this.append = append;
    }

    public boolean isNext() {
        return this.next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public String getRange() {
        return this.range;
    }

    public void setRange(String range) {
        this.range = range;
    }
}

