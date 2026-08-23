package cc.bugcat.bcdt.logsev.modules.reader.files.search;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.files.IReaderMethod;
import cc.bugcat.bcdt.logsev.modules.reader.files.ReadLineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MovePointVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.LineInfo;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.ReaderInfoVo;
import java.util.List;


/**
 * 搜索模式下，跳转到搜索结果集末尾
 *
 * @author bugcat-cc
 * */
class GotoFileEndMethod implements IReaderMethod<MovePointVi> {
    
    
    private final ReaderSearchManage manage;

    GotoFileEndMethod(ReaderSearchManage manage) {
        this.manage = manage;
    }

    @Override
    public Object execute(ReaderFileSession reader, MovePointVi reqVi) throws Exception {
        
        if ( manage.hasMore() ) {
            throw BusinessException.builder(Errors.ERROR_PARAM, "匹配结果超过1000，请输入更多搜索条件！");
        }
        
        ReaderInfoVo resp = ReaderInfoVo.build(reqVi, "body");
        int size = this.manage.getSize();
        if ( size == 0 ) {
            return resp;
        }
        
        LogFileBean fileBean = reader.getFileBean();
        int rows = reqVi.getRows();
        int offsetStart = size - rows;
        
        List<LineInfo> lines = this.manage.limit(offsetStart, size);
        resp.setLines(lines);
        
        LineInfo lastInfo = lines.getLast();
        ReadLineInfo lastLine = this.manage.getLine((int) lastInfo.getLineStart());
        long curEnd = lastLine.getLineEnd();
        if ( curEnd == fileBean.getOpenSize() ) {
            resp.setPercent(100);
        } else {
            resp.setPercent((int)(lastLine.getLineEnd() * 100L / fileBean.getOpenSize()));
        }
        return resp;
    }
}

