package cc.bugcat.bcdt.logsev.modules.reader.handle;

import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSessionFactory;
import cc.bugcat.bcdt.logsev.modules.reader.files.AbstractReaderManage;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.ReaderInfoVi;
import cc.bugcat.bcdt.logsev.websocket.ReceiveMethod;
import org.springframework.stereotype.Service;


/**
 * 关闭文件
 *
 * @author bugcat-cc
 * */
@Service
public class ReadStopHandle extends AbstractMethodHandle<ReaderInfoVi> {
    
    public ReadStopHandle() {
        super(ReceiveMethod.readStop);
    }

    @Override
    protected Object execute(AccountSession session, ReaderInfoVi reqVi) {
        ReaderFileSessionFactory factory = session.getReaderFactory();
        ReaderFileSession reader = factory.openReader(reqVi);
        AbstractReaderManage manage = reader.getReaderManage();
        manage.setReadStop();
        return null;
    }
}

