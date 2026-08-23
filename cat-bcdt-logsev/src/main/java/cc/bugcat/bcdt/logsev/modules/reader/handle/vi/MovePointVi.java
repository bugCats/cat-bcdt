package cc.bugcat.bcdt.logsev.modules.reader.handle.vi;


/**
 * 移动到文件开始、末尾
 *
 * @author bugcat-cc
 * */
public class MovePointVi extends ReaderInfoVi {

    //客户端窗口第一行的开始索引
    private long pageStart;


    //客户端窗口最后一行的结束索引
    private long pageEnd;

    
    
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
}

