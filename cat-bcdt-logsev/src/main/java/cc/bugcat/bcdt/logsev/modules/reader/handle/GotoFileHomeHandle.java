package cc.bugcat.bcdt.logsev.modules.reader.handle;

import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSessionFactory;
import cc.bugcat.bcdt.logsev.modules.reader.files.AbstractReaderManage;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MovePointVi;
import cc.bugcat.bcdt.logsev.websocket.ReceiveMethod;
import org.springframework.stereotype.Service;


/**
 * 跳转到文件开头
 *
 * @author bugcat-cc
 * */
@Service
public class GotoFileHomeHandle extends AbstractMethodHandle<MovePointVi> {
    public GotoFileHomeHandle() {
        super(ReceiveMethod.gotoFileHome);
    }

    @Override
    protected Object execute(AccountSession session, MovePointVi reqVi) throws Exception {
        ReaderFileSessionFactory factory = session.getReaderFactory();
        ReaderFileSession reader = factory.openReader(reqVi);
        AbstractReaderManage manage = reader.getReaderManage();
        return manage.gotoFileHome(session).execute(reader, reqVi);
    }
}

