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
 * 跳转到末尾
 *
 * @author bugcat-cc
 * */
class GotoFileEndMethod implements IReaderMethod<MovePointVi> {
    
    private final ReaderLineManage manage;

    
    GotoFileEndMethod(ReaderLineManage manage) {
        this.manage = manage;
    }

    
    @Override
    public Object execute(ReaderFileSession reader, MovePointVi reqVi) throws Exception {

        LogFileBean fileBean = reader.getFileBean();
        
        ReaderInfoVo resp = ReaderInfoVo.build(reqVi, "body");
        resp.setPercent(100);
        resp.setFileInfo(fileBean.toReaderFileInfo());
        
        long fileSize = manage.getFileSize();
        if ( fileSize == 0 ) {
            return resp;
        }
        if (reqVi.getPageEnd() == fileSize) {
            return resp;
        }
        
        // 客户端当前显示行数
        int rows = reqVi.getRows();

        MoveInfo moved = manage.movePrev(fileSize, rows, fileSize, rows);
        ReadLine readLine = manage.createReadLine();

        int offsetStart = moved.getOffsetStart();
        int offsetEnd = moved.getOffsetEnd();

        // reqVi.getPageEnd()是上一次文件大小，fileSize为当前文件大小，
        // 如果incrementSize有值，说明日志文件后续新增内容，本次跳转到末尾，需要加载最新内容，从最新末尾开始计算
        long bufferSize = offsetEnd - offsetStart;
        long incrementSize = fileSize - reqVi.getPageEnd(); 
        if ( incrementSize < bufferSize) {
            offsetStart = this.manage.toRelative(reqVi.getPageEnd());
            resp.setAppend("tail");
        }

        List<ReadLineInfo> list = ReaderHandle.readLines(readLine, offsetStart, offsetEnd);
        List<LineInfo> lines = ReaderHandle.toLineInfo(list);
        resp.setLines(lines);
        
        return resp;
    }
}

