package cc.bugcat.bcdt.logsev.modules.reader.files.line;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;
import cc.bugcat.bcdt.logsev.modules.reader.files.*;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MoveLineVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MovePointVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.OpenFileVi;
import java.nio.channels.FileChannel;

/**
 * 按行读取：上下一行、首页、尾页；
 * 
 * 先按curStart、curEnd读取内容缓存到bytes
 * 
 * 后续按pointStart、pointEnd读取时，如果读取索引在 [curStart, curEnd) 区间中，直接读取缓存bytes；
 * 
 * 如果不在区间中，移动curStart、curEnd，使其包含pointStart、pointEnd；
 *
 * @author bugcat-cc
 * */
public class ReaderLineManage extends AbstractReaderManage {
    
    /**
     * 当前阅读文件对象信息
     * */
    private final LogFileBean logInfo;
    
    /**
     * 上一次刷新文件时，文件大小
     * */
    private long fileSize = 0L;

    /**
     * 读取内容缓存
     * */
    private byte[] bytes;
    
    /**
     * 读取内容缓存的开始索引，包含当前
     * */
    private long curStart;
    
    /**
     * 读取内容缓存的结束索引，不包含
     * */
    private long curEnd;

    
    
    public ReaderLineManage(LogFileBean logInfo) {
        this.logInfo = logInfo;
    }

    
    /**
     * 重置阅读指针
     * */
    public void resetPoint() {
        this.curStart = 0L;
        this.curEnd = 0L;
        this.bytes = null;
        this.fileSize = this.logInfo.getOpenSize();
    }

    
    /**
     * 移动缓存索引
     * @param pointStart 开始读索引
     * @param pointEnd   结束读索引
     * */
    public synchronized void movePoint(long pointStart, long pointEnd) throws Exception {
        if (pointStart < this.curStart || pointEnd > this.curEnd) {
            this.reloadByte(pointStart, pointEnd);
        }
    }

    /**
     * 读取索引 point，是否在 [curStart, curEnd) 区间中
     * */
    private boolean between(long point) {
        return point >= this.curStart && point <= this.curEnd;
    }

    
    /**
     * 读取内容
     * */
    private synchronized void reloadByte(long pointStart, long pointEnd) throws Exception {
        this.fileSize = logInfo.getOpenSize();
        FileChannel channel = this.logInfo.getChannel();
        
        long start = Math.max(pointStart - ReaderHandle.line_size, 0L);
        ReadLineInfo startLine = ReaderHandle.findPrevEnter(channel, start, this.fileSize);
        this.curStart = startLine.getLineStart();
        
        long end = Math.min(pointEnd + ReaderHandle.line_size, this.fileSize);
        ReadLineInfo endLine = ReaderHandle.findNextEnter(channel, end, this.fileSize);
        this.curEnd = endLine.getLineEnd();
        
        this.bytes = ReaderHandle.readByte(channel, this.curStart, this.curEnd);
    }

    
    /**
     * 从指定位置，跳过多少个换行
     * @param point 指定索引
     * @param rows 换行符数量；小于 0 向前；大于 0 向后；
     * */
    private long skip(long point, int rows) {
        if ( !this.between(point) ) {
            return -1L;
        }
        
        long result = -1L;
        if (rows > 0) {
            int forStart = this.toRelative(point);
            int forEnd = this.toRelative(this.curEnd);
            for (int idx = forStart; idx < forEnd; ++idx) {
                if (this.bytes[idx] != 10 || --rows != 0) continue;
                return this.toAbsolute(idx + 1);
            }
            if ( this.curEnd == this.fileSize ) {
                return this.fileSize;
            }
        } else if (rows < 0) {
            rows = -rows;
            int forStart = this.toRelative(point);
            for (int idx = ReaderHandle.trimBegin(this.bytes, forStart - 1); idx >= 0; --idx) {
                if (this.bytes[idx] != 10 || --rows != 0) continue;
                return this.toAbsolute(idx + 1);
            }
            if (this.curStart == 0L) {
                return 0L;
            }
        } else {
            throw BusinessException.builder("500", Errors.ERROR_PARAM, "错误的 rows：" + rows);
        }
        return result;
    }

    
    /**
     * curStart 向前移动
     * */
    protected MoveInfo movePrev(long point, int skip, long blockEnd, int rows) throws Exception {
        long curPrev = this.skip(point, -skip);
        if (curPrev < 0L) {
            ReadLineInfo line = ReaderHandle.findPrevEnter(rows, this.logInfo.getChannel(), point, this.fileSize);
            this.movePoint(line.getLineStart(), blockEnd);
            curPrev = this.skip(point, -skip);
            if (curPrev < 0L) {
                this.curStart = 0L;
                this.curEnd = 0L;
                curPrev = 0L;
            }
        }
        this.movePoint(curPrev, point);
        int offsetStart = this.toRelative(curPrev);
        int offsetEnd = this.toRelative(point);
        return new MoveInfo(offsetStart, offsetEnd);
    }

    
    /**
     * curEnd 向后移动
     * */
    protected MoveInfo moveNext(long point, int skip, long blockStart, int rows) throws Exception {
        long curNext = this.skip(point, skip);
        if ( curNext < 0L ) {
            ReadLineInfo line = ReaderHandle.findNextEnter(rows, this.logInfo.getChannel(), point, this.fileSize);
            this.movePoint(blockStart, line.getLineEnd());
            curNext = this.skip(point, skip);
            if (curNext < 0L) {
                this.curStart = this.fileSize;
                this.curEnd = this.fileSize;
                curNext = this.fileSize;
            }
        }
        this.movePoint(point, curNext);
        int offsetStart = this.toRelative(point);
        int offsetEnd = this.toRelative(curNext);
        return new MoveInfo(offsetStart, offsetEnd);
    }

    
    /**
     * 转换为绝对索引
     * */
    protected long toAbsolute(long point) {
        return point + this.curStart;
    }

    /**
     * 转换成相对索引
     * */
    protected int toRelative(long point) {
        return (int)(point - this.curStart);
    }
    

    protected long getFileSize() {
        return this.fileSize;
    }


    
    public ReadLine createReadLine() {
        ReadLine line = new ReadLine();
        line.setLogInfo(logInfo);
        line.setBytes(bytes);
        line.setPoint(curStart);
        return line;
    }
    
    
    @Override
    public int getReadMod() {
        return 10;
    }

    
    
    @Override
    public void closeFile() {
        this.resetPoint();
    }

    
    
    /**
     * 打开文件
     * */
    @Override
    public IReaderMethod<OpenFileVi> openFile(AccountSession session) {
        return new OpenFileMethod(this);
    }


    /**
     * 跳转到首页
     * */
    @Override
    public IReaderMethod<MovePointVi> gotoFileHome(AccountSession session) {
        return new GotoFileHomeMethod(this);
    }


    /**
     * 跳转到末尾
     * */
    @Override
    public IReaderMethod<MovePointVi> gotoFileEnd(AccountSession session) {
        return new GotoFileEndMethod(this);
    }


    /**
     * 上下移动行
     * */
    @Override
    public IReaderMethod<MoveLineVi> moveLine(AccountSession session) {
        return new MoveLineMethod(this);
    }
}

