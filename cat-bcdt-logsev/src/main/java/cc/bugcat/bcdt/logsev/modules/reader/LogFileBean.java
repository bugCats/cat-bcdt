package cc.bugcat.bcdt.logsev.modules.reader;

import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vo.ReaderFileInfo;
import cc.bugcat.bcdt.logsev.utils.Tools;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Date;


/**
 * 当前正在查看的文件
 *
 * @author bugcat-cc
 * */
public class LogFileBean {
    
    /**
     * 应用id
     * */
    private final String appId;
    
    /**
     * 文件名
     * */
    private final String logName;
    
    /**
     * 文件对象
     * */
    private final File logFile;

    /**
     * 文件字符编码
     * */
    private final Charset charset;
    
    /**
     * 随机读取对象
     * */
    private final RandomAccessFile randomFile;
    
    /**
     * io流管道
     * */
    private final FileChannel channel;
    
    /**
     * 读取时间戳
     * */
    private long openTime = 0L;
    
    /**
     * 读取时文件大小
     * */
    private long openSize = 0L;

    
    
    public LogFileBean(String appId, File logFile) throws Exception {
        this.appId = appId;
        this.logName = logFile.getName();
        this.logFile = logFile;
        this.charset = Charset.forName(Tools.detectEncoding(logFile));
        this.randomFile = new RandomAccessFile(logFile, "r");
        this.channel = this.randomFile.getChannel();
        this.openTime = System.currentTimeMillis();
        this.openSize = logFile.length();
    }

    /**
     * 客户端关闭窗口时，关闭io流
     * */
    protected void destroy() {
        Tools.closeQuietly(this.channel, this.randomFile);
    }

    
    /**
     * 客户端刷新文件，重新获取文件信息
     * */
    public long reloadSize() {
        this.openTime = System.currentTimeMillis();
        this.openSize = this.logFile.length();
        return this.openSize;
    }

    
    /**
     * 转文件信息对象
     * */
    public ReaderFileInfo toReaderFileInfo() {
        ReaderFileInfo info = new ReaderFileInfo();
        info.setLogName(this.logName);
        info.setOpenTime(DateUtil.rs.format(new Date(this.openTime), "yyyy-MM-dd HH:mm:ss.SSS"));
        info.setOpenSize(Tools.sizeTo(this.openSize));
        return info;
    }

    
    
    
    public String getAppId() {
        return this.appId;
    }

    public String getLogName() {
        return this.logName;
    }

    public Charset getCharset() {
        return charset;
    }

    public FileChannel getChannel() {
        return this.channel;
    }

    public long getOpenTime() {
        return this.openTime;
    }

    public long getOpenSize() {
        return this.openSize;
    }
}

