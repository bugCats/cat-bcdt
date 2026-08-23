package cc.bugcat.bcdt.logsev.web.vo;

/**
 * 应用信息
 *
 * @author bugcat-cc
 * */
public class AppsVo {
    
    /**
     * 应用id
     * */
    private String appId;
    
    /**
     * 应用名
     * */
    private String appName;
    
    /**
     * 所属命名空间
     * */
    private String label;

    
    public String getAppId() {
        return this.appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return this.appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLabel() {
        return this.label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
}

