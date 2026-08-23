package cc.bugcat.bcdt.logsev.modules.reader.files;

import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.LimiterLineVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MoveLineVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.MovePointVi;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.OpenFileVi;
import java.io.Closeable;


/**
 *
 * @author bugcat-cc
 * */
public abstract class AbstractReaderManage implements Closeable {
    
    /**
     * 关闭窗口，释放资源
     * */
    @Override
    public final void close() {
        this.closeFile();
    }

    
    /**
     * 获取当前阅读模式
     * */
    public abstract int getReadMod();

    /**
     * 终止当前阅读模式
     * */
    public void setReadStop() {
    
    }

    
    /**
     * 关闭释放资源
     * */
    public abstract void closeFile();

    
    /**
     * 打开文件
     * */
    public IReaderMethod<OpenFileVi> openFile(AccountSession session) {
        return null;
    }

    
    /**
     * 跳转到首页
     * */
    public IReaderMethod<MovePointVi> gotoFileHome(AccountSession session) {
        return null;
    }

    
    /**
     * 跳转到末尾
     * */
    public IReaderMethod<MovePointVi> gotoFileEnd(AccountSession session) {
        return null;
    }

    
    /**
     * 上下移动行
     * */
    public IReaderMethod<MoveLineVi> moveLine(AccountSession session) {
        return null;
    }

    
    /**
     * 在指定区间内分页
     * */
    public IReaderMethod<LimiterLineVi> limiterLine(AccountSession session) {
        return null;
    }
    
    
    /**
     * 在指定区间内分页移动
     * */
    public IReaderMethod<MoveLineVi> limiterLineMore(AccountSession session) {
        return null;
    }
}

