/**
 * Copyright (C), 2017-2019, 北京壹嘉商旅
 *
 * @author: Administrator
 * @create: 2019/10/15 9:20
 * Desc:
 */
package cc.bugcat.jinkens.plugin.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 
 * Jenkins 工作目录；
 * 
 * 在 ${dirPath}/${dirName} 中，应该可以看见 {@code config.xml}、{@code /jobs/} 文件与文件夹；
 * 
 * */
@Component("pluginJenkinsConfig")
public class JenkinsConfig {

    /**
     * Jenkins 的工作目录地址
     * */
    @Value("${jenkins.dirPath:E:\\jenkins}")
    private String dirPath;
    
    
    /**
     * Jenkins 的工作目录名称
     * */
    @Value("${jenkins.dirName:workspace}")
    private String dirName;

    
    
    @Override
    public String toString() {
        return "JenkinsConfig{home=" + dirPath + ", dir=" + dirName + "}";
    }


    public String getDirPath() {
        return dirPath;
    }
    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getDirName() {
        return dirName;
    }
    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

}
