package cc.bugcat.bcdt.logsev.modules.reader.handle;

import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSessionFactory;
import cc.bugcat.bcdt.logsev.modules.reader.files.AbstractReaderManage;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MovePointVi;
import cc.bugcat.bcdt.logsev.websocket.ReceiveMethod;
import org.springframework.stereotype.Service;


/**
 * 跳转到文件末尾
 *
 * @author bugcat-cc
 * */
@Service
public class GotoFileEndHandle extends AbstractMethodHandle<MovePointVi> {
    public GotoFileEndHandle() {
        super(ReceiveMethod.gotoFileEnd);
    }

    @Override
    protected Object execute(AccountSession session, MovePointVi reqVi) throws Exception {
        ReaderFileSessionFactory factory = session.getReaderFactory();
        ReaderFileSession reader = factory.openReader(reqVi);
        AbstractReaderManage manage = reader.getReaderManage();
        return manage.gotoFileEnd(session).execute(reader, reqVi);
    }
}

