package cc.bugcat.bcdt.logsev.config;

/**
 *
 * @author bugcat-cc
 * */
public class AppInstanceInfo {

    private String instanceId;

    // 实例所在文件目录
    private String appDir;
    
    // 实例端口
    private String appPort;


    public AppInstanceInfo(String instanceId, String appDir, String appPort){
        this.instanceId = instanceId;
        this.appDir = appDir;
        this.appPort = appPort;
    }

    
    public String getInstanceId() {
        return instanceId;
    }
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getAppDir() {
        return appDir;
    }
    public void setAppDir(String appDir) {
        this.appDir = appDir;
    }

    public String getAppPort() {
        return appPort;
    }
    public void setAppPort(String appPort) {
        this.appPort = appPort;
    }
}
