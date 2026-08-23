package cc.bugcat.bcdt.logsev.modules.reader.files.search;

import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;
import cc.bugcat.bcdt.logsev.modules.reader.files.IReaderMethod;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.OpenFileVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.SearchKey;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.ReaderInfoVo;
import java.util.List;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


/**
 * 开始执行搜索
 *
 * @author bugcat-cc
 * */
class OpenFileMethod implements IReaderMethod<OpenFileVi> {
    private final ReaderSearchManage manage;
    private AccountSession session;

    OpenFileMethod(ReaderSearchManage manage, AccountSession session) {
        this.manage = manage;
        this.session = session;
    }

    @Override
    public Object execute(ReaderFileSession reader, OpenFileVi reqVi) throws Exception {
        List<SearchKey> searchKeys = reqVi.getSearchKeys();
        this.manage.openSearch(searchKeys);
        Mono.fromRunnable(() -> this.manage.postSearch(this.session, reqVi)).publishOn(Schedulers.single()).subscribe();
        return ReaderInfoVo.build(reqVi, "tail");
    }
}


