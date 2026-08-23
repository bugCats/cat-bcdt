package cc.bugcat.bcdt.logsev.config;


import cc.bugcat.acore.framework.json.JsonTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author bugcat-cc
 * */
@Component
public class LoggerConfig {

    
    public static final String SPLIT = "@#";
    
    private static LoggerConfig config;

    // 实例名称、目录关系
    private List<AppWorkspace> workspace;
    
    
    private final Map<String, String> pathMap = new HashMap<>();

    
    // 服务名 实例ip端口
    private final Map<String, AppInstance> instanceMap = new HashMap<>();
    
    
    public LoggerConfig(@Value("${logger.config}") String conf, AppConfig appConfig){
        config = this;
 
        this.workspace = JsonTools.parseArray(conf, AppWorkspace.class);
        for ( AppWorkspace ws : workspace ) {
            String label = ws.getLabel();
            List<String> apps = ws.getApps();
            for(String appName : apps){
                String appDir = ws.getDir() + "/" + appName;
                pathMap.put(label + SPLIT + appName, appDir);

                AppInstance instance = instanceMap.get(appName);
                if( instance == null ){
                    instance = new AppInstance();
                    instance.setAppName(appName);
                    instance.setWorkspace(new ArrayList<>(3));
                    instanceMap.put(appName, instance);
                }
            }
        }
    }


    public static LoggerConfig getConfig() {
        return config;
    }
    
    
    public String getPath(String appName){
        return pathMap.getOrDefault(appName, "");
    }
   
    public List<AppWorkspace> getWorkspace() {
        return workspace;
    }

    public Map<String, AppInstance> getInstanceMap() {
        return instanceMap;
    }
}
