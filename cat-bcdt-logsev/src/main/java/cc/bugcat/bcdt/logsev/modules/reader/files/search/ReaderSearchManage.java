package cc.bugcat.bcdt.logsev.modules.reader.files.search;

import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.utils.MdEncrypt;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.acore.framework.utils.ThreadPool;
import cc.bugcat.acore.framework.utils.WeakMapCache;
import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;
import cc.bugcat.bcdt.logsev.modules.reader.files.AbstractReaderManage;
import cc.bugcat.bcdt.logsev.modules.reader.files.IReaderMethod;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReadLineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReaderIterator;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.*;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.LineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.ReaderInfoVo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.ReaderSearchVo;
import cc.bugcat.bcdt.logsev.websocket.ReaderSocket;
import cc.bugcat.bcdt.logsev.websocket.ReplyMethod;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;


/**
 *
 * @author bugcat-cc
 * */
public class ReaderSearchManage extends AbstractReaderManage {
    
    /**
     * 搜索结果集大小
     * */
    private static final int MAX = 500;
    
    /**
     * 上一次搜索缓存
     * */
    private static final WeakMapCache<CacheEntry> resultMap = new WeakMapCache<>();
    
    /**
     * 文件对象信息
     * */
    private final LogFileBean logInfo;
    
    /**
     * 搜索类
     * */
    private final SearchHandle searchHandle = new SearchHandle();
    
    /**
     * 搜索关键词
     * */
    private List<GroupMatch> predicate = new ArrayList<>();
    
    /**
     * 本次搜索关键词md5值
     * */
    private String searchKey = "";
    
    /**
     * 是否使用了缓存
     * */
    private boolean useCache = false;

    
    
    public ReaderSearchManage(LogFileBean fileBean) {
        this.logInfo = fileBean;
    }

    
    
    public void reset() {
        this.searchHandle.clear();
        this.searchKey = "";
        this.useCache = false;
        this.predicate.clear();
    }

    
    /**
     * 客户点击搜索操作
     * */
    public void openSearch(List<SearchKey> searchKeys) {
        
        this.searchHandle.preHandle();
        
        if ( ObjectUtil.isEmpty(searchKeys)) {
            return;
        }
        
        List<GroupMatch> groups = new ArrayList<>(searchKeys.size());
        Set<String> groupKeys = new TreeSet<>();
        
        for (SearchKey search : searchKeys) {
            
            Set<String> andKeys = new TreeSet<>();
            Optional.ofNullable(search.getAndKeys()).orElseGet(Collections::emptySet).stream().filter(Objects::nonNull).forEach(andKeys::add);
            
            Set<String> notKeys = new TreeSet<>();
            Optional.ofNullable(search.getNotKeys()).orElseGet(Collections::emptySet).stream().filter(Objects::nonNull).forEach(notKeys::add);
            
            if ( andKeys.isEmpty() && notKeys.isEmpty() ) {
                continue;
            }
            
            AndMatch andMatch = new AndMatch(andKeys);
            NotMatch notMatch = new NotMatch(andKeys);
            
            GroupMatch groupMatch = new GroupMatch(andMatch, notMatch);
            groups.add(groupMatch);
            StringBuilder colKey = new StringBuilder();
            for (String key : andKeys) {
                colKey.append(key + ",");
            }
            for (String key : notKeys) {
                colKey.append(key + ",");
            }
            groupKeys.add(colKey.toString());
        }
        
        String md5 = MdEncrypt.md5(String.join(",", groupKeys));
        this.useCache = md5.equals(this.searchKey);
        if ( !this.useCache ) {
            this.reset();
            this.searchKey = md5;
            this.predicate.addAll(groups);
        }
        
        CacheEntry entry = resultMap.get(this.searchKey);
        if ( entry != null ) {
            this.useCache = true;
            this.searchHandle.result = entry.result;
            this.searchHandle.hasMore = entry.hasMore;
            this.searchHandle.executeTime = entry.executeTime;
        }
    }

    /**
     * 异步线程进行搜索
     * */
    public void postSearch(AccountSession session, OpenFileVi reqVi) {
        
        if ( this.predicate.isEmpty() ) {
            return;
        }

        try {

            Visit visit = new Visit(session, reqVi);

            //说明已经搜索完毕
            if (this.useCache) {
                int end = Math.min(searchHandle.result.size(), reqVi.getRows());
                for (int idx = 0; idx < end; idx ++) {
                    ReadLineInfo line = searchHandle.result.get(idx);
                    visit.reply(line);
                }
                
            } else {
                //读取文件，逐行比较
                this.searchHandle.postHandle(visit);
            }
            
            ReaderSearchVo resp = new ReaderSearchVo();
            resp.setAppId(reqVi.getAppId());
            resp.setLogName(reqVi.getLogName());
            resp.setTotal(this.getSize());
            resp.setHasMore(this.searchHandle.hasMore);
            long time = this.searchHandle.executeTime;
            if (time > 1000L) {
                resp.setTime(ObjectUtil.doubleScale((time / 1000.0), 2) + "s");
            } else {
                resp.setTime(time + "ms");
            }
            ResultEntity<Object> body = ResultEntity.ok(resp);
            session.sendData(ReaderSocket.build(ReplyMethod.searchInfo, body));
        } catch (Exception ex) {
            ResultEntity<Void> resp = ResultEntity.fail(BusinessException.builder(ex));
            session.sendData(resp.toJson());
        }
    }

    
    /**
     * 从搜索结果集中获取指定行内容
     * */
    public List<LineInfo> limit(int offsetStart, int offsetEnd) {
        return this.searchHandle.limit(offsetStart, offsetEnd);
    }

    /**
     * 是否已经超过最大数量
     * */
    public boolean hasMore() {
        return this.searchHandle.hasMore;
    }

    /**
     * 搜索结果集
     * */
    public int getSize() {
        return this.searchHandle.result == null ? 0 : this.searchHandle.result.size();
    }

    
    /**
     * 获取指定行信息
     * */
    public ReadLineInfo getLine(int idx) {
        if (idx < 0 || idx > this.getSize()) {
            return null;
        }
        return this.searchHandle.result.get(idx);
    }

    
    /**
     * 判断内容是否满足条件
     * */
    private boolean testKeywords(String text) {
        for (GroupMatch group : this.predicate) {
            if ( !group.test(text) ) continue;
            return true;
        }
        return false;
    }


    /**
     * 获取当前阅读模式
     * */
    @Override
    public int getReadMod() {
        return 30;
    }

    
    /**
     * 终止当前阅读模式
     * */
    @Override
    public void setReadStop() {
        this.searchHandle.interrupt = true;
    }


    /**
     * 关闭释放资源
     * */
    @Override
    public void closeFile() {
        this.reset();
    }


    /**
     * 打开文件
     * */
    @Override
    public IReaderMethod<OpenFileVi> openFile(AccountSession session) {
        return new OpenFileMethod(this, session);
    }


    /**
     * 跳转到首页
     * */
    @Override
    public IReaderMethod<MovePointVi> gotoFileHome(AccountSession session) {
        return this.decorator(new GotoFileHomeMethod(this));
    }


    /**
     * 跳转到末尾
     * */
    @Override
    public IReaderMethod<MovePointVi> gotoFileEnd(AccountSession session) {
        return this.decorator(new GotoFileEndMethod(this));
    }


    /**
     * 上下移动行
     * */
    @Override
    public IReaderMethod<MoveLineVi> moveLine(AccountSession session) {
        return this.decorator(new MoveLineMethod(this));
    }


    /**
     * 在指定区间内分页
     * */
    @Override
    public IReaderMethod<LimiterLineVi> limiterLine(AccountSession session) {
        return this.decorator(new LimiterLineMethod(this));
    }


    /**
     * 在指定区间内分页移动
     * */
    @Override
    public IReaderMethod<MoveLineVi> limiterLineMore(AccountSession session) {
        return this.decorator(new LimiterLineMoreMethod(this));
    }

    
    
    
    
    private <T> IReaderMethod<T> decorator(IReaderMethod<T> readerMethod) {
        return (reader, reqVi) -> {
            if (this.searchHandle.working) {
                return ReaderInfoVo.build((ReaderInfoVi) reqVi, "tail");
            }
            return readerMethod.execute(reader, reqVi);
        };
    }

    
    
    private class Visit {
        
        private final Consumer<String> sendData;
        private final OpenFileVi reqVi;
        private int index = 0;

        private Visit(Consumer<String> sendData, OpenFileVi reqVi) {
            this.sendData = sendData;
            this.reqVi = reqVi;
        }
        
        /**
         * 每搜索到一行数据，下发消息到客户端
         * */
        public void reply(ReadLineInfo line) {
            if (this.index < this.reqVi.getRows()) {
                ReaderInfoVo resp = ReaderInfoVo.build(this.reqVi, "tail");
                if (this.index == 1) {
                    resp.setPercent(GotoFileHomeMethod.getPercent(line, ReaderSearchManage.this.logInfo.getOpenSize()));
                }
                List<LineInfo> lines = new ArrayList<>(1);
                LineInfo info = new LineInfo();
                info.setLineStart(this.index);
                info.setLineEnd(this.index + 1);
                info.setText(line.getText());
                lines.add(info);
                resp.setLines(lines);
                this.index ++;
                
                ResultEntity<Object> body = ResultEntity.ok(resp);
                this.sendData.accept(ReaderSocket.build(ReplyMethod.searchResult, body));
            }
        }
    }
    
    

    private class SearchHandle {
        
        //是否正在执行搜索
        private volatile boolean working = false;
        
        //是否触发了中断
        private volatile boolean interrupt = false;
        
        //搜索结果集
        private List<ReadLineInfo> result = null;
        
        //是否超过最大
        private boolean hasMore = false;
        
        //搜索耗时
        private long executeTime = 0L;

        
        private SearchHandle() { }

        //重置
        private void clear() {
            this.executeTime = 0L;
            this.result = null;
            this.hasMore = false;
        }

        //如果已经在搜索，触发中断结束上一次搜索，重新搜索
        private void preHandle() {
            while (this.working) {
                this.interrupt = true;
                ThreadPool.sleep(50L);
            }
        }

        // 执行搜索
        private void postHandle(Visit visit) throws Exception {
            long start = System.currentTimeMillis();
            this.interrupt = false;
            this.result = new ArrayList<>(MAX);
            ReaderIterator iterator = ReaderIterator.readAllLine(logInfo);
            while ( iterator.hasNext() ) {
                ReadLineInfo line = iterator.next();
                String text = line.getText();
                if ( testKeywords(text) ) {
                    visit.reply(line);
                    this.result.add(line);
                }
                if (this.result.size() >= MAX) {
                    this.hasMore = iterator.hasNext();
                    break;
                }
                if (this.interrupt) {
                    break;
                }
            }
            if ( !this.interrupt ) {
                resultMap.put(searchKey, new CacheEntry(this.result, this.hasMore, this.executeTime));
            }
            this.executeTime = System.currentTimeMillis() - start;
            this.interrupt = false;
            this.working = false;
        }

        private List<LineInfo> limit(int offsetStart, int offsetEnd) {
            if (offsetStart < 0) {
                offsetStart = 0;
            }
            if (offsetEnd > this.result.size()) {
                offsetEnd = this.result.size();
            }
            int rows = offsetEnd - offsetStart;
            if ( rows <= 0 ) {
                return Collections.emptyList();
            }
            List<LineInfo> lines = new ArrayList<>(rows);
            for (int idx = offsetStart; idx < offsetEnd; ++idx) {
                LineInfo info = new LineInfo();
                info.setLineStart(idx);
                info.setLineEnd(idx + 1);
                info.setText(this.result.get(idx).getText());
                lines.add(info);
            }
            return lines;
        }
    }

    private static class CacheEntry {
        private final List<ReadLineInfo> result;
        private final boolean hasMore;
        private final long executeTime;

        private CacheEntry(List<ReadLineInfo> result, boolean hasMore, long executeTime) {
            this.result = result;
            this.hasMore = hasMore;
            this.executeTime = executeTime;
        }
    }

    private static class NotMatch implements Predicate<String> {
        private final Set<String> notKeys;

        private NotMatch(Set<String> notKeys) {
            this.notKeys = notKeys;
        }

        @Override
        public boolean test(String text) {
            for (String andKey : this.notKeys) {
                if (!text.contains(andKey)) continue;
                return true;
            }
            return false;
        }
    }

    private static class AndMatch implements Predicate<String> {
        private final Set<String> andKeys;

        private AndMatch(Set<String> andKeys) {
            this.andKeys = andKeys;
        }

        @Override
        public boolean test(String text) {
            for (String andKey : this.andKeys) {
                if (text.contains(andKey)) continue;
                return false;
            }
            return true;
        }
    }

    private static class GroupMatch {
        private final Predicate<String> andKeys;
        private final Predicate<String> notKeys;

        private GroupMatch(Predicate<String> andKeys, Predicate<String> notKeys) {
            this.andKeys = andKeys;
            this.notKeys = notKeys;
        }

        private boolean test(String text) {
            return this.andKeys.test(text) && this.notKeys.test(text);
        }
    }
}

