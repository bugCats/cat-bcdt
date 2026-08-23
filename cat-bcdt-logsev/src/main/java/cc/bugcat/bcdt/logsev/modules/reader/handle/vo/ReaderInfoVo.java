package cc.bugcat.bcdt.logsev.modules.reader.handle.vo;

import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.ReaderInfoVi;

import java.util.Collections;
import java.util.List;

/**
 * 客户端窗口文件对象
 *
 * @author bugcat-cc
 * */
public class ReaderInfoVo {
    
    //应用Id
    private String appId;
    
    //文件名
    private String logName;
    
    //追加模式
    private String append;
    
    //当前读取的索引值占整个文件的百分比进度
    private Integer percent;
    
    //文件信息
    private ReaderFileInfo fileInfo;
    
    
    //当前读取的内容
    private List<LineInfo> lines;

    
    
    public static ReaderInfoVo build(ReaderInfoVi req, String append) {
        ReaderInfoVo vo = new ReaderInfoVo();
        vo.setAppId(req.getAppId());
        vo.setLogName(req.getLogName());
        vo.setAppend(append);
        vo.setLines(Collections.emptyList());
        return vo;
    }

    public String getAppId() {
        return this.appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getLogName() {
        return this.logName;
    }
    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getAppend() {
        return this.append;
    }
    public void setAppend(String append) {
        this.append = append;
    }

    public Integer getPercent() {
        return this.percent;
    }
    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public ReaderFileInfo getFileInfo() {
        return this.fileInfo;
    }
    public void setFileInfo(ReaderFileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public List<LineInfo> getLines() {
        return this.lines;
    }
    public void setLines(List<LineInfo> lines) {
        this.lines = lines;
    }
}

