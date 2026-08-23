package cc.bugcat.bcdt.logsev.modules.reader.files.search;

import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.files.IReaderMethod;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReaderHandle;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReadLineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.LimiterLineVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.LimiterLineVo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.LineInfo;
import java.util.Collections;
import java.util.List;


/**
 * 在指定区间中上下移动行
 *
 * @author bugcat-cc
 * */
class LimiterLineMethod implements IReaderMethod<LimiterLineVi> {
    
    private final ReaderSearchManage manage;

    LimiterLineMethod(ReaderSearchManage manage) {
        this.manage = manage;
    }

    @Override
    public Object execute(ReaderFileSession reader, LimiterLineVi reqVi) throws Exception {
        
        LimiterLineVo resp = new LimiterLineVo();
        resp.setAppId(reqVi.getAppId());
        resp.setLogName(reqVi.getLogName());
        resp.setAppend("body");
        resp.setLineStart(0L);
        resp.setLines(Collections.emptyList());
        
        if (this.manage.getSize() == 0) {
            return resp;
        }
        
        int idx = (int) reqVi.getLineStart();
        ReadLineInfo line = this.manage.getLine(idx);
        if ( line == null ) {
            return resp;
        }
        
        resp.setLineStart(line.getLineStart());
        LogFileBean fileBean = reader.getFileBean();
        long fileSize = fileBean.getOpenSize();
        if (fileSize == 0) {
            return resp;
        }

        ReadLineInfo startLine = ReaderHandle.findPrevEnter(reqVi.getBefore(), fileBean.getChannel(), line.getLineStart(), fileBean.getOpenSize());
        ReadLineInfo endLine = ReaderHandle.findNextEnter(reqVi.getAfter(), fileBean.getChannel(), line.getLineEnd(), fileBean.getOpenSize());
        List<ReadLineInfo> list = ReaderHandle.readLines(fileBean, startLine.getLineStart(), endLine.getLineEnd());
        List<LineInfo> lines = ReaderHandle.toLineInfo(list);
        resp.setLines(lines);
        return resp;
    }
}

