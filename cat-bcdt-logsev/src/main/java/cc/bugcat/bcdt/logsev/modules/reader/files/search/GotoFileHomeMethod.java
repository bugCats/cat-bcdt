package cc.bugcat.bcdt.logsev.modules.reader.files.search;

import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.files.IReaderMethod;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReadLineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MovePointVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.LineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.ReaderInfoVo;
import java.util.List;


/**
 * 搜索模式下，跳转到搜索结果集首页
 *
 * @author bugcat-cc
 * */
class GotoFileHomeMethod implements IReaderMethod<MovePointVi> {
    
    private final ReaderSearchManage manage;

    GotoFileHomeMethod(ReaderSearchManage manage) {
        this.manage = manage;
    }
    
    

    @Override
    public Object execute(ReaderFileSession reader, MovePointVi reqVi) throws Exception {
        LogFileBean fileBean = reader.getFileBean();
        ReaderInfoVo resp = this.gotoFileHome(fileBean, reqVi);
        return resp;
    }

    
    
    public ReaderInfoVo gotoFileHome(LogFileBean fileBean, MovePointVi reqVi) {
        
        ReaderInfoVo resp = ReaderInfoVo.build(reqVi, "body");
        
        int size = this.manage.getSize();
        if ( size == 0 ) {
            return resp;
        }

        int rows = reqVi.getRows();
        List<LineInfo> lines = this.manage.limit(0, rows);
        LineInfo firstInfo = lines.getFirst();
        resp.setLines(lines);
        
        ReadLineInfo firstLine = this.manage.getLine((int) firstInfo.getLineStart());
        resp.setPercent(GotoFileHomeMethod.getPercent(firstLine, fileBean.getOpenSize()));
        return resp;
    }

    
    public static int getPercent(ReadLineInfo first, long fileSize) {
        long curStart = first.getLineStart();
        if (curStart == 0L) {
            return 0;
        }
        return Math.min((int)(curStart * 100L / fileSize), 1);
    }
}

