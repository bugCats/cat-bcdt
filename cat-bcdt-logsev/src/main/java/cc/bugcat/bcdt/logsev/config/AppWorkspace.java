package cc.bugcat.bcdt.logsev.config;

import java.util.List;


/**
 *
 * @author bugcat-cc
 * */
public class AppWorkspace {

    /**
     * 实例顶级目录  /work/prodboot
     * */
    private String dir;
    
    /**
     * 顶级目录显示名称
     * */
    private String label;
    
    /**
     * 顶级目录中包含的实例名称；
     * 通过${dir}/${app}可以获取到项目工作目录；${dir}/${app}/logs 为项目日志文件目录
     * */
    private List<String> apps;
    


    public String getDir() {
        return dir;
    }
    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getApps() {
        return apps;
    }
    public void setApps(List<String> apps) {
        this.apps = apps;
    }
}
