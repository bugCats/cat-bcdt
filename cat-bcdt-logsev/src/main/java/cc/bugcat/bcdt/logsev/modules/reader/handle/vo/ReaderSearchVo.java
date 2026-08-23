package cc.bugcat.bcdt.logsev.modules.reader.handle.vo;

/**
 * 搜索模式结果
 *
 * @author bugcat-cc
 * */
public class ReaderSearchVo extends ReaderInfoVo {
    
    //搜索结果集数量
    private int total;
    
    //是否超过最大值
    private boolean hasMore;
    
    //搜索耗时
    private String time;

    
    
    public int getTotal() {
        return this.total;
    }
    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isHasMore() {
        return this.hasMore;
    }
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}

