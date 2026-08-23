package cc.bugcat.bcdt.logsev.modules;

import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.logsev.config.AppConfig;
import cc.bugcat.bcdt.logsev.utils.Tools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.*;
import java.util.*;


/**
 * JVM启动参数：-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/work/heapdump/${appName}_${appPort}.hprof
 * 则监听/work/heapdump/中文件变化，如果有实例发生内存溢出，会在此目录中生成堆信息，触发消息事件；
 * 
 * 1. 其他项目配置内存溢出文件
 * 2. logger项目启动监听文件夹内容变化
 * 3. 解析文件名，发送钉钉通知
 *
 * @author bugcat-cc
 * */
@Component
public class HeapdumpHandler implements InitializingBean {
    
    private static Logger log = LoggerFactory.getLogger(Tools.class);
    
    @Autowired
    private AppConfig appConfig;

    
    @Override
    public void afterPropertiesSet() {
        if ( appConfig.isHeapDumpListenEnable() ) {
            if( StringUtils.isNoneBlank(appConfig.getHeapDumpDirPath(), appConfig.getHeapDumpDingToken()) ){
                Thread thread = new FolderListener(appConfig);
                thread.start();
            }
        }
    }

    
    
    
    private static class FolderListener extends Thread {
        
        private final String dirPath;
        private final String token;
        private FolderListener(AppConfig appConfig) {
            this.dirPath = appConfig.getHeapDumpDirPath();
            this.token = appConfig.getHeapDumpDingToken();
        }

        @Override
        public void run() {
            
            log.warn("监听文件夹：{}", dirPath);
            final Path path = Paths.get(dirPath);

            Map<String, Long> sendTimeMap = new HashMap<>();
            
            try( WatchService watchService = FileSystems.getDefault().newWatchService()) {
                path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,  StandardWatchEventKinds.ENTRY_MODIFY);
                while ( true ) {
                    final WatchKey key = watchService.take();
                    Set<String> fileNames = new HashSet<>();
                    for ( WatchEvent<?> watchEvent : key.pollEvents() ) {
                        final WatchEvent.Kind<?> kind = watchEvent.kind();
                        if ( kind == StandardWatchEventKinds.OVERFLOW ) {
                            continue;
                        }
                        final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
                        final Path filename = watchEventPath.context();
                        fileNames.add(filename.toString()); //堆文件名
                    }
                    if ( !key.reset() ) {
                        break;
                    }

                    List<String> systemInfos = new ArrayList<>();
                    long nowTime = System.currentTimeMillis();
                    for ( String fileName : fileNames ) {
                        String systemInfo = StringUtils.substringBeforeLast(fileName, ".");
                        long lastTime = sendTimeMap.getOrDefault(systemInfo, 0L);
                        if( nowTime - lastTime > 300000 ){ //5分钟
                            systemInfos.add(systemInfo);
                            sendTimeMap.put(systemInfo, nowTime);
                        }
                    }
                    
                    if( systemInfos.size() > 0 ){
                        String date = DateUtil.ll.getDate();
                        Tools.Markdown markdown = Tools.buildMarkdown()
                                .title("严重异常[" + date + "]")
                                .needAt(true)
                                .subject("**内存泄露** 警告：[" + date + "]");

                        for ( String systemInfo : systemInfos ) {
                            //todo 堆文件格式：${appName}_${appPort}.hprof 如果不是这个格式，需要在此次自行处理 
                            String[] info = systemInfo.split("_");  //系统名_端口
                            markdown.items("> 实例【" + info[0] + "#" + info[1] + "】内存泄露，请及时处理！");
                        }
                        Tools.dingSend(token, markdown);
                    }
                }
            } catch(Exception ex) {
                log.error(BusinessException.getStackTrace(ex));
            }
        }
    }

 
}
