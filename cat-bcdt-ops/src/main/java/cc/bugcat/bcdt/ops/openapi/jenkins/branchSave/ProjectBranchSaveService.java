package cc.bugcat.bcdt.ops.openapi.jenkins.branchSave;


import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.acore.http.HttpUtil;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.config.JenkinsConfig;
import cc.bugcat.bcdt.ops.openapi.jenkins.branchSave.vi.ProjectBranchSaveVi;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class ProjectBranchSaveService extends AbstractOpenApiService<List<ProjectBranchSaveVi>, Void> {
    
    @Autowired
    private JenkinsConfig jenkinsConfig;
    
    
    @Override
    public Void execute(List<ProjectBranchSaveVi> reqVi, RequestContext context) {

        if ( ObjectUtil.isEmpty(reqVi) ) {
            return null;
        }
        
        UserInfo user = UserUtil.getUser(context);
        String now = DateUtil.ll.getDate();
        reqVi.forEach(vi -> vi.setEditUser(user.getRealname() + " " + now ));

        String source = HttpUtil.creart().jsonPost().send(jenkinsConfig.getPlugin() + "/jenkins/saveBranch", reqVi);
        List<String> paths = JsonTools.parseArray(source, String.class);

        String jenkinsAuth = "Basic " + jenkinsConfig.getAuth();;
        String reload = jenkinsConfig.getHost() + "/#job#/reload";
        HttpUtil.PostMethod post = HttpUtil.creart().timeOut(-1, -1).headers("Authorization", jenkinsAuth).doPost();
        Map<String, Object> param = new HashMap<>();

        for ( String path : paths ) {
            try {
                String url = reload.replace("#job#", path);
                post.send(url, param);
                Thread.sleep(100L);
            } catch ( Exception ex ) {
                System.out.println(BusinessException.getStackTrace(ex));
            }
        }
        
        return null;
    }
}
