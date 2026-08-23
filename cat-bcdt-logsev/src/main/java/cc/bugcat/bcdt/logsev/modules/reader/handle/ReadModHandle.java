package cc.bugcat.bcdt.logsev.modules.reader.handle;

import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.reader.LogFileBean;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSessionFactory;
import cc.bugcat.bcdt.logsev.modules.reader.files.AbstractReaderManage;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.ReadModVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.ReaderAtTimeVo;
import cc.bugcat.bcdt.logsev.websocket.ReceiveMethod;
import org.springframework.stereotype.Service;


/**
 * 修改阅读模式
 *
 * @author bugcat-cc
 * */
@Service
public class ReadModHandle extends AbstractMethodHandle<ReadModVi> {
    
    public ReadModHandle() {
        super(ReceiveMethod.readMod);
    }

    @Override
    protected Object execute(AccountSession session, ReadModVi reqVi) {
        ReaderFileSessionFactory factory = session.getReaderFactory();
        ReaderFileSession reader = factory.openReader(reqVi);
        LogFileBean fileBean = reader.getFileBean();
        AbstractReaderManage manage = reader.getReaderManage();
        switch (reqVi.getReadMod()) {
            case 10: {
                if (manage.getReadMod() == 10) break;
                reader.setReaderLine();
                break;
            }
            case 20: {
                if (manage.getReadMod() == 20) break;
                reader.setReaderAtTime();
                break;
            }
            case 30: {
                if (manage.getReadMod() == 30) break;
                reader.setSearch();
                break;
            }
            default: {
                reader.setReaderLine();
            }
        }
        ReaderAtTimeVo resp = new ReaderAtTimeVo();
        resp.setReadMod(reader.getReadMod());
        resp.setFileSize(fileBean.getOpenSize());
        return resp;
    }
}

