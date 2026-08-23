package cc.bugcat.bcdt.logsev.modules.reader.handle;

import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSessionFactory;
import cc.bugcat.bcdt.logsev.modules.reader.files.AbstractReaderManage;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.LimiterLineVi;
import cc.bugcat.bcdt.logsev.websocket.ReceiveMethod;
import org.springframework.stereotype.Service;


/**
 * 搜索模式下，显示结果行的前后n行数据
 *
 * @author bugcat-cc
 * */
@Service
public class LimiterLineHandle extends AbstractMethodHandle<LimiterLineVi> {
    
    
    public LimiterLineHandle() {
        super(ReceiveMethod.limiterLine);
    }

    @Override
    protected Object execute(AccountSession session, LimiterLineVi reqVi) throws Exception {
        ReaderFileSessionFactory factory = session.getReaderFactory();
        ReaderFileSession reader = factory.openReader(reqVi);
        AbstractReaderManage manage = reader.getReaderManage();
        return manage.limiterLine(session).execute(reader, reqVi);
    }
}

