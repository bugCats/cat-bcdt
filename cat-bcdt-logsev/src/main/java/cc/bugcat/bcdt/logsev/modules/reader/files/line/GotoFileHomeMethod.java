package cc.bugcat.bcdt.logsev.modules.reader.files.line;

import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.files.IReaderMethod;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReadLine;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReaderHandle;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReadLineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MovePointVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.LineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.ReaderInfoVo;
import java.util.List;


/**
 * 跳转到首页
 *
 * @author bugcat-cc
 * */
class GotoFileHomeMethod implements IReaderMethod<MovePointVi> {
    
    private final ReaderLineManage manage;

    GotoFileHomeMethod(ReaderLineManage manage) {
        this.manage = manage;
    }

    @Override
    public Object execute(ReaderFileSession reader, MovePointVi reqVi) throws Exception {
        LogFileBean fileBean = reader.getFileBean();
        
        ReaderInfoVo resp = ReaderInfoVo.build(reqVi, "body");
        resp.setPercent(0);
        resp.setFileInfo(fileBean.toReaderFileInfo());
        
        if (reqVi.getPageStart() == 0L) {
            return resp;
        }
        
        long fileSize = manage.getFileSize();
        if  (fileSize == 0L) {
            return resp;
        }

        // 客户端当前显示行数
        int rows = reqVi.getRows();
        
        MoveInfo moved = manage.moveNext(0L, rows, 0L, rows);
        ReadLine readLine = manage.createReadLine();

        int offsetStart = moved.getOffsetStart();
        int offsetEnd = moved.getOffsetEnd();

        long bufferSize = offsetEnd - offsetStart;
        long incrementSize = reqVi.getPageStart();
        if (incrementSize < bufferSize) {
            offsetEnd = manage.toRelative(reqVi.getPageStart());
            resp.setAppend("head");
        }
        
        List<ReadLineInfo> list = ReaderHandle.readLines(readLine, offsetStart, offsetEnd);
        List<LineInfo> lines = ReaderHandle.toLineInfo(list);
        resp.setLines(lines);
        return resp;
    }
}

