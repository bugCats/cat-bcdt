package cc.bugcat.bcdt.logsev.modules.reader.handle;

import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSessionFactory;
import cc.bugcat.bcdt.logsev.modules.reader.files.AbstractReaderManage;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MoveLineVi;
import cc.bugcat.bcdt.logsev.websocket.ReceiveMethod;
import org.springframework.stereotype.Service;


/**
 * 搜索模式下，显示结果行的前后n行数据
 *
 * @author bugcat-cc
 * */
@Service
public class LimiterLineMoreHandle extends AbstractMethodHandle<MoveLineVi> {
    
    
    public LimiterLineMoreHandle() {
        super(ReceiveMethod.limiterLineMore);
    }

    @Override
    protected Object execute(AccountSession session, MoveLineVi reqVi) throws Exception {
        ReaderFileSessionFactory factory = session.getReaderFactory();
        ReaderFileSession reader = factory.openReader(reqVi);
        AbstractReaderManage manage = reader.getReaderManage();
        return manage.limiterLineMore(session).execute(reader, reqVi);
    }
}

