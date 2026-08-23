package cc.bugcat.bcdt.logsev.modules.reader.files.search;

import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.files.IReaderMethod;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReaderHandle;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReadLineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MoveLineVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.LineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.ReaderInfoVo;
import java.util.List;


/**
 * 在指定区间内上下翻页
 *
 * @author bugcat-cc
 * */
public class LimiterLineMoreMethod implements IReaderMethod<MoveLineVi> {
    
    private final ReaderSearchManage manage;

    LimiterLineMoreMethod(ReaderSearchManage manage) {
        this.manage = manage;
    }

    @Override
    public Object execute(ReaderFileSession reader, MoveLineVi reqVi) throws Exception {
        
        ReaderInfoVo resp = ReaderInfoVo.build(reqVi, reqVi.getAppend());
        LogFileBean fileBean = reader.getFileBean();
        
        long fileSize = fileBean.getOpenSize();
        if ( fileSize == 0L ) {
            return resp;
        }

        final int rows = reqVi.getRows();
        final boolean next = reqVi.isNext();
        
        long curStart = 0L;
        long curEnd = 0L;
        if (next) {
            curStart = reqVi.getPageEnd();
            ReadLineInfo endLine = ReaderHandle.findNextEnter(rows, fileBean.getChannel(), curStart, fileBean.getOpenSize());
            curEnd = endLine.getLineEnd();
        } else {
            curEnd = reqVi.getPageStart();
            ReadLineInfo startLine = ReaderHandle.findPrevEnter(rows, fileBean.getChannel(), curEnd, fileBean.getOpenSize());
            curStart = startLine.getLineStart();
        }
        
        List<ReadLineInfo> list = ReaderHandle.readLines(fileBean, curStart, curEnd);
        List<LineInfo> lines = ReaderHandle.toLineInfo(list);
        resp.setLines(lines);
        return resp;
    }
}

