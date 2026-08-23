package cc.bugcat.bcdt.logsev.modules.reader.handle;

import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSessionFactory;
import cc.bugcat.bcdt.logsev.modules.reader.files.AbstractReaderManage;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.OpenFileVi;
import cc.bugcat.bcdt.logsev.websocket.ReceiveMethod;
import org.springframework.stereotype.Service;


/**
 * 打开文件
 *
 * @author bugcat-cc
 * */
@Service
public class OpenFileHandle extends AbstractMethodHandle<OpenFileVi> {
    
    public OpenFileHandle() {
        super(ReceiveMethod.openFile);
    }

    @Override
    protected Object execute(AccountSession session, OpenFileVi reqVi) throws Exception {
        ReaderFileSessionFactory factory = session.getReaderFactory();
        ReaderFileSession reader = factory.openReader(reqVi);
        AbstractReaderManage manage = reader.getReaderManage();
        return manage.openFile(session).execute(reader, reqVi);
    }
}

