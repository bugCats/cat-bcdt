package cc.bugcat.bcdt.logsev.modules.reader.files;


import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;

import java.util.Iterator;

/**
 * 向后解析文件，返回行
 *
 * @author bugcat-cc
 * */
public class ReaderIterator implements Iterator<ReadLineInfo> {
    
    /**
     * 文件信息
     * */
    private final LogFileBean logInfo;
    
    /**
     * 当前文件大小
     * */
    private final long fileSize;

    /**
     * 每次读取的缓存
     * */
    private byte[] bytes;
    
    /**
     * 每次读取缓存的大小
     * */
    private int length = 0;
    
    /**
     * 每次读取缓存的绝对开始索引
     * */
    private long curStart = 0L;
    
    /**
     * 每次读取缓存的绝对结束索引
     * */
    private long curEnd = 0L;
    
    /**
     * 相对bytes的索引
     * */
    private int offset = 0;

    
    
    public static ReaderIterator readAllLine(LogFileBean logInfo) throws Exception {
        return new ReaderIterator(logInfo);
    }
    
    
    private ReaderIterator(LogFileBean logInfo) throws Exception {
        this.logInfo = logInfo;
        this.fileSize = logInfo.getOpenSize();
        this.reset();
    }

    
    public void reset() throws Exception {
        this.curStart = 0L;
        this.curEnd = 0L;
        this.bytes = null;
        this.offset = 0;
        this.length = 0;
        this.loadData();
    }

    
    
    @Override
    public boolean hasNext() {
        return this.curStart + this.offset < this.fileSize;
    }

    
    
    @Override
    public ReadLineInfo next() {
        try {
            //当相对偏移量超过缓存大小，移动窗口再次加载缓存
            if (this.offset >= this.length) {
                this.loadData();
            }
            
            int start = this.offset;
            int end = this.findEnter(this.offset);
            if (end == -1) { //已经读取到文件末尾，结束
                this.offset = this.length;
                return null;
            }
            
            ReadLineInfo line = new ReadLineInfo();
            line.setLineStart(this.curStart + start);
            line.setLineEnd(this.curStart + end + 1L);
            line.setText(new String(this.bytes, start, end - start + 1, logInfo.getCharset()));
            this.offset = end + 1;
            return line;
            
        } catch (Exception ex) {
            throw BusinessException.builder(ex);
        }
    }

    /**
     * 先后找换行符
     * */
    private int findEnter(int start) {
        int max = this.length - 1;
        for (int idx = start; idx < this.length; ++idx) {
            if (this.bytes[idx] != 10 && idx != max) continue;
            return idx;
        }
        return -1;
    }

    
    /**
     * 读取缓存
     * */
    private void loadData() throws Exception {
        this.curStart = this.curEnd;
        ReadLineInfo next = ReaderHandle.findNextEnter(logInfo.getChannel(), this.curStart + ReaderHandle.iterator_size, this.fileSize);
        this.curEnd = next.getLineEnd();
        this.bytes = ReaderHandle.readByte(logInfo.getChannel(), this.curStart, this.curEnd);
        this.length = this.bytes.length;
        this.offset = 0;
    }
}

