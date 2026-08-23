package cc.bugcat.bcdt.logsev.modules.reader.files.line;

import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.files.IReaderMethod;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MovePointVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.OpenFileVi;


/**
 * 打开文件，跳转到末尾
 *
 * @author bugcat-cc
 * */
class OpenFileMethod implements IReaderMethod<OpenFileVi> {
    
    private final ReaderLineManage manage;

    OpenFileMethod(ReaderLineManage manage) {
        this.manage = manage;
    }
    

    @Override
    public Object execute(ReaderFileSession reader, OpenFileVi reqVi) throws Exception {
        
        LogFileBean fileBean = reader.getFileBean();
        fileBean.reloadSize();
        manage.resetPoint();
        
        GotoFileEndMethod fileMethod = new GotoFileEndMethod(manage);
        MovePointVi move = new MovePointVi();
        move.setRows(reqVi.getRows());
        move.setAppId(reqVi.getAppId());
        move.setLogName(reqVi.getLogName());
        return fileMethod.execute(reader, move);
    }
}

