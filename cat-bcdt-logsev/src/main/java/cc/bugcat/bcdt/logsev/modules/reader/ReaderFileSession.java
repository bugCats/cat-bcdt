package cc.bugcat.bcdt.logsev.modules.reader;

import cc.bugcat.bcdt.logsev.modules.reader.files.AbstractReaderManage;
import cc.bugcat.bcdt.logsev.modules.reader.files.line.ReaderLineManage;
import cc.bugcat.bcdt.logsev.modules.reader.files.search.ReaderSearchManage;
import cc.bugcat.bcdt.logsev.modules.reader.files.time.ReaderAtTimeManage;
import cc.bugcat.bcdt.logsev.utils.Tools;

import java.util.function.Consumer;

/**
 * 当前正在查看的文件会话
 *
 * @author bugcat-cc
 * */
public class ReaderFileSession {
    
    /**
     * 当前正在查看的文件
     * */
    private final LogFileBean fileBean;
    
    
    /**
     * 翻页模式
     * */
    private final ReaderLineManage readLineManage;
    
    /**
     * 实时读取模式
     * */
    private final ReaderAtTimeManage readerTimeManage;
    
    
    /**
     * 搜索模式
     * */
    private final ReaderSearchManage searchManage;
    
    
    /**
     * 当前正在使用的模式
     * */
    private AbstractReaderManage readerManage = null;
    

    
    public ReaderFileSession(LogFileBean fileBean) {
        this.fileBean = fileBean;
        this.readLineManage = new ReaderLineManage(fileBean);
        this.readerTimeManage = new ReaderAtTimeManage(fileBean);
        this.searchManage = new ReaderSearchManage(fileBean);
        this.readerManage = this.readLineManage;
    }


    /**
     * 客户端关闭窗口时，释放资源
     * */
    protected void destroy() {
        Tools.closeQuietly(this.readLineManage, this.readerTimeManage, this.searchManage);
        this.fileBean.destroy();
        this.readerManage = null;
    }

    
    /**
     * 如果是实时读取，异步线程获取内容之后，下发内容到客户端
     * */
    public void readerAtTime(Consumer<String> sendData) {
        if (this.readerManage.getReadMod() != 20) {
            return;
        }
        this.readerTimeManage.readTime(sendData);
    }

    
    /**
     * 修改成翻页模式
     * */
    public void setReaderLine() {
        this.readerManage.close();
        this.readLineManage.resetPoint();
        this.readerManage = this.readLineManage;
    }

    
    /**
     * 修改为实时读取模式
     * */
    public void setReaderAtTime() {
        this.readerManage.close();
        this.readerTimeManage.reload();
        this.readerManage = this.readerTimeManage;
    }

    
    /**
     * 修改为搜索模式
     * */
    public void setSearch() {
        this.readerManage.close();
        this.searchManage.reset();
        this.readerManage = this.searchManage;
    }

    
    /**
     * 获取当前模式
     * */
    public AbstractReaderManage getReaderManage() {
        return this.readerManage;
    }

    
    /**
     * 获取当前读取模式
     * */
    public int getReadMod() {
        return this.readerManage.getReadMod();
    }

    
    /**
     * 获取当前读取的文件名
     * */
    public String getLogName() {
        return this.fileBean.getLogName();
    }


    /**
     * 获取当前读取的文件信息对象
     * */
    public LogFileBean getFileBean() {
        return this.fileBean;
    }
    
    
}

