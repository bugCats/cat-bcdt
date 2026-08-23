package cc.bugcat.bcdt.logsev.modules.reader.handle.vi;

import java.util.List;

/**
 * 打开文件，开始阅读
 *
 * @author bugcat-cc
 * */
public class OpenFileVi extends ReaderInfoVi {
    
    
    //如果是搜索模式，则为搜索关键词
    private List<SearchKey> searchKeys;

    
    public List<SearchKey> getSearchKeys() {
        return this.searchKeys;
    }
    public void setSearchKeys(List<SearchKey> searchKeys) {
        this.searchKeys = searchKeys;
    }


}

