package cc.bugcat.bcdt.logsev.modules.reader.files.search;

import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.files.IReaderMethod;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReadLineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MoveLineVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.LineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.ReaderInfoVo;
import java.util.List;


/**
 * 上下移动行
 *
 * @author bugcat-cc
 * */
class MoveLineMethod implements IReaderMethod<MoveLineVi> {
    
    private final ReaderSearchManage manage;

    MoveLineMethod(ReaderSearchManage manage) {
        this.manage = manage;
    }

    @Override
    public Object execute(ReaderFileSession reader, MoveLineVi reqVi) throws Exception {
        
        ReaderInfoVo resp = ReaderInfoVo.build(reqVi, reqVi.getAppend());
        
        int size = this.manage.getSize();
        if ( size == 0 ) {
            return resp;
        }

        final int rows = reqVi.getRows();
        final boolean next = reqVi.isNext();
        final int moveRows = "line".equals(reqVi.getRange()) ? 1 : rows - 1;

        int offsetStart = 0;
        int offsetEnd = 0;
        if (next) { //下一行、下一页
            offsetStart = (int) reqVi.getPageEnd();
            offsetEnd = offsetStart + moveRows;
        } else { //上一行、上一页
            offsetEnd = (int) reqVi.getPageStart();
            offsetStart = offsetEnd - moveRows;
        }
        List<LineInfo> lines = this.manage.limit(offsetStart, offsetEnd);
        resp.setLines(lines);
        if ( lines.isEmpty() ) {
            return resp;
        }

        final LogFileBean fileBean = reader.getFileBean();
        final long fileSize = fileBean.getOpenSize();
        
        LineInfo firstInfo = lines.getFirst();
        ReadLineInfo firstLine = this.manage.getLine((int)firstInfo.getLineStart());
        long curStart = firstLine.getLineStart();
        
        LineInfo lastInfo = lines.getLast();
        ReadLineInfo lastLine = this.manage.getLine((int)lastInfo.getLineStart());
        long curEnd = lastLine.getLineEnd();
        
        if ( curEnd == fileSize ) {
            resp.setPercent(100);
        } else if (curStart == 0L) {
            resp.setPercent(0);
        } else {
            if (next) {
                resp.setPercent((int)(curEnd * 100L / fileSize));
            } else {
                resp.setPercent((int)(curStart * 100L / fileSize));
            }
            if (resp.getPercent() < 1) {
                resp.setPercent(1);
            }
        }
        return resp;
    }
}

