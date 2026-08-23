package cc.bugcat.bcdt.ops.openapi.jenkins.branchList;


import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.acore.http.HttpUtil;
import cc.bugcat.bcdt.ops.config.JenkinsConfig;
import cc.bugcat.bcdt.ops.openapi.jenkins.branchList.vo.GroupProject;
import cc.bugcat.bcdt.ops.openapi.jenkins.branchList.vo.ProjectBranchListVo;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class ProjectBranchListService extends AbstractOpenApiService<Void, List<ProjectBranchListVo>> {
    
    @Autowired
    private JenkinsConfig jenkinsConfig;
    
    
    @Override
    public List<ProjectBranchListVo> execute(Void reqVi, RequestContext context) {
        
        String source = HttpUtil.creart().doPost().send(jenkinsConfig.getPlugin() + "/jenkins/branchList", new HashMap<>());
        
        List<ProjectBranchListVo> resp = JsonTools.parseArray(source, ProjectBranchListVo.class);
        
        Optional.ofNullable(resp)
                .orElseGet(ArrayList::new)
                .forEach(branch -> {
                    List<GroupProject> projects = Optional.ofNullable(branch.getDictDeps())
                            .orElseGet(HashMap::new)
                            .entrySet()
                            .stream()
                            .map(entry -> {
                                GroupProject project = new GroupProject();
                                project.setDirName(entry.getKey());
                                project.setDictDeps(entry.getValue());
                                return project;
                            })
                            .sorted(Comparator.comparing(GroupProject::getDirName))
                            .collect(Collectors.toList());
                    branch.setGroupProjects(projects);
                });
        
        return resp;
        
    }
}
