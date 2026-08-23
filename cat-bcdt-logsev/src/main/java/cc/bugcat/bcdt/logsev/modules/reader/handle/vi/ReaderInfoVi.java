package cc.bugcat.bcdt.logsev.modules.reader.handle.vi;


import jakarta.validation.constraints.NotBlank;

/**
 * 读取文件
 *
 * @author bugcat-cc
 * */
public class ReaderInfoVi {
    
    //客户端窗口可以显示行数
    private int rows = 10;
    
    @NotBlank(message="appId不能为空")
    private String appId;
   
    @NotBlank(message="logName不能为空")
    private String logName;

    
    
    public int getRows() {
        return this.rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
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
}

