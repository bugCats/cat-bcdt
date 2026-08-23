/**
 * Copyright (C), 2017-2019, 北京壹嘉商旅
 *
 * @author: Administrator
 * @create: 2019/8/2 11:54
 * Desc:
 */
package cc.bugcat.jinkens.plugin.ctrl;


import cc.bugcat.jinkens.plugin.conf.JenkinsConfig;
import cc.bugcat.jinkens.plugin.util.XmlUtil;
import cc.bugcat.jinkens.plugin.vi.GitBranchSaveVi;
import cc.bugcat.jinkens.plugin.vo.GitBranchListVo;
import cc.bugcat.jinkens.plugin.vo.GitBranchListVo.Depository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Jenkins管理
 * */
@RestController
public class JenkinsCtrl implements InitializingBean {


    private static final String utf8 = "utf-8";

    private static String jenkinsDir;
    private static String jenkinsHome;
    
    @Autowired
    private JenkinsConfig jenkinsConfig;


    @Override
    public void afterPropertiesSet() throws Exception {
        
        System.out.println(jenkinsConfig.toString());
        
        jenkinsDir = jenkinsConfig.getDirName();
        jenkinsHome = jenkinsConfig.getDirPath() + "/" + jenkinsConfig.getDirName();
    }


    /**
     * 查看所有的仓库所在分支
     * */
    @RequestMapping(value = "/plugin/jenkins/branchList", method = RequestMethod.POST)
    public ResponseEntity<List<GitBranchListVo>> branchList() {
        List<GitBranchListVo> resp = new ArrayList<>();
        try {
            // 全局配置文件
            File viewConfigFile = new File(jenkinsHome + "/config.xml");
            String viewConfig = FileUtils.readFileToString(viewConfigFile, utf8);

            //所有的项目目录
            String projectRoot = jenkinsHome + "/jobs/";

            //视图
            List<String> views = XmlUtil.queryNodes(viewConfig.replaceAll("\\s+", " "), "hudson views listView");
            //视图中包含的项目
            Map<String, List<String>> envProject = new HashMap<>();
            for ( String listView : views ) {
                String view = XmlUtil.queryValue(listView, "listView description");
                
                List<String> projectName = XmlUtil.queryValues(listView, "listView jobNames string").stream().filter(str -> !str.contains("/")).collect(Collectors.toList());
                //视图中，没有设置项目，检查是否使用了正则表达式
                if ( CollectionUtils.isEmpty(projectName) ) {
                    String includeRegex = XmlUtil.queryValue(listView, "listView includeRegex");
                    if ( XmlUtil.isNotBlank(includeRegex) ) { //说明是使用正则表达式
                        File projectRootDir = new File(projectRoot);
                        projectRootDir.listFiles((dir, fileName) -> {
                            Pattern filter = Pattern.compile(includeRegex);
                            if ( filter.matcher(fileName).find() ) {
                                projectName.add(fileName);
                            }
                            return false;
                        });
                    }
                }
                envProject.put(view, projectName);       //获取到所有任务文件夹名称
            }
            for ( Map.Entry<String, List<String>> entry : envProject.entrySet() ) {

                GitBranchListVo vo = new GitBranchListVo();
                vo.setGroupName(entry.getKey());

                Map<String, List<Depository>> dictDeps = new HashMap<>();   //文件夹-项目
                List<String> projectNames = entry.getValue();
                for ( String pn : projectNames ) {

                    File pjDir = new File(projectRoot + "/" + pn);

                    List<File> configs = new LinkedList<>();    //所有的配置文件

                    File jobs = new File(pjDir.getPath() + "/jobs/");    //jobs文件夹存在，说这个是一个文件夹，下面还有子项目
                    if ( !jobs.exists() ) {
                        jobs = pjDir;
                    }
                    configFiles(jobs, configs); //得到所有的config.xml文件

                    if ( configs != null && configs.size() > 0 ) {
                        List<Depository> depositorys = new ArrayList<>();
                        //所有的配置文件
                        for ( File file : configs ) {
                            String xml = FileUtils.readFileToString(file, utf8);
                            String description = XmlUtil.queryValue(xml, "description");
                            if ( StringUtils.isBlank(description) || description.startsWith("@") ) {
                                continue;
                            }
                            String[] des = description.split("@"); 
                            String env = XmlUtil.queryValue(xml, "branches hudson.plugins.git.BranchSpec name");
                            Depository depository = new Depository();
                            depository.setConfigPath(file.getPath());
                            depository.setProjectName(des[0]);
                            depository.setBranchName(env);
                            if( des.length > 1 ) {
                                depository.setEditUser(des[1]);
                            }
                            depositorys.add(depository);
                        }
                        if ( depositorys != null && depositorys.size() > 0 ) {
                            dictDeps.put(pn, depositorys);
                        }
                    }
                }
                vo.setDictDeps(dictDeps);
                resp.add(vo);
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(resp);
    }


    /**
     * 编辑仓库分支
     * */
    @RequestMapping(value = "/plugin/jenkins/saveBranch", method = RequestMethod.POST)
    public ResponseEntity<Set<String>> saveBranch(@RequestBody List<GitBranchSaveVi> req) {
        Set<String> paths = null;
        if ( req != null && req.size() > 0 ) {
            paths = new HashSet<>();
            for ( GitBranchSaveVi vi : req ) {
                try {
                    File config = new File(vi.getFpath());
                    String xml = FileUtils.readFileToString(config, utf8);
                    String project = XmlUtil.queryValue(xml, "description").split("@")[0];
                    String change = XmlUtil.findAndReplaceNode(xml, "description", project + "@" + vi.getEditUser());
                    change = XmlUtil.findAndReplaceNode(change, "branches hudson.plugins.git.BranchSpec name", vi.getBranch());
                    FileUtils.write(config, change, utf8, false);

                    String path = XmlUtil.substringBetween(vi.getFpath().replace("\\", "/"), "/" + jenkinsDir + "/", "/config.xml").replace("jobs", "job");
                    paths.add(path);
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }
        return ResponseEntity.ok(paths);
    }
    

    private void configFiles(File dir, List<File> configs) {
        if ( dir != null && dir.exists() && dir.isDirectory() ) {
            File[] files = dir.listFiles();
            if( files != null && files.length > 0 ){
                for ( File file : files ) {
                    if ( file.isDirectory() && !",modules,builds,".contains("," + file.getName() + ",") ) { //modules builds 文件夹中是编译信息，不需要
                        configFiles(file, configs);
                    } else {
                        if ( "config.xml".equalsIgnoreCase(file.getName()) ) {
                            configs.add(file);
                        }
                    }
                }
            }
        }
    }


}
