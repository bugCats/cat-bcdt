package cc.bugcat.bcdt.ops.openapi.gitlab;

import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.WorkPool;
import cc.bugcat.bcdt.ops.openapi.gitlab.vi.GitlabHooksPullVi;
import cc.bugcat.bcdt.ops.service.inter.GitlabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class GitlabHookService extends AbstractOpenApiService<GitlabHooksPullVi, Void> {
    
    
    @Autowired
    private GitlabService gitlabService;
    
    @Override
    public Void execute(GitlabHooksPullVi reqVi, RequestContext context) {
        
        if( "merge_request".equals(reqVi.getEventType()) ){
            WorkPool.createThread(() -> {
                gitlabService.pullRequest(reqVi);
            });
        }
        
        return null;
        
    }
}
