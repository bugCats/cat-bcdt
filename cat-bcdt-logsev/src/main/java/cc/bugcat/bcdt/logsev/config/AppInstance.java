package cc.bugcat.bcdt.logsev.config;

import java.util.List;


/**
 *
 * @author bugcat-cc
 * */
public class AppInstance {

    /**
     * 实例名称
     * */
    private String appName;
    
    
    /**
     * 文件路径，相对于 app lib 文件夹
     * */
    private List<AppInstanceInfo> workspace;

    
    

    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<AppInstanceInfo> getWorkspace() {
        return workspace;
    }
    public void setWorkspace(List<AppInstanceInfo> workspace) {
        this.workspace = workspace;
    }

}
