package cc.bugcat.bcdt.ops.openapi.gitea;

import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.WorkPool;
import cc.bugcat.bcdt.ops.service.inter.GiteaService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class GiteaHookService extends AbstractOpenApiService<String, Void> {
    
    
    @Autowired
    private GiteaService giteaService;
    
    
    @Override
    public Void execute(String reqVi, RequestContext context) {
        HttpServletRequest request = context.getRequest();
        final String event = StringUtils.trimToEmpty(request.getHeader("X-Gitea-Event")).toLowerCase();
        WorkPool.createThread(() -> {
            switch ( event ) {
                case "push" ->    //推送
                        giteaService.push(reqVi);
                case "create" ->  // 创建分支、标签
                        giteaService.create(reqVi);
                case "pull_request" ->  // 合并请求
                        giteaService.pullRequest(reqVi);
                case "issue_comment" ->  //评论
                        giteaService.issueComment(reqVi);
            }
        });
        return null;
    }
}
