package cc.bugcat.bcdt.logsev.modules.reader.handle.vi;

import java.util.Set;


/**
 * 搜索模式下，查询关键词
 *
 * @author bugcat-cc
 * */
public class SearchKey {
    
    //需要包含的关键词
    private Set<String> andKeys;
    
    //需要排除的关键词
    private Set<String> notKeys;

    
    public Set<String> getAndKeys() {
        return this.andKeys;
    }
    public void setAndKeys(Set<String> andKeys) {
        this.andKeys = andKeys;
    }

    public Set<String> getNotKeys() {
        return this.notKeys;
    }
    public void setNotKeys(Set<String> notKeys) {
        this.notKeys = notKeys;
    }
}

