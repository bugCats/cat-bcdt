package cc.bugcat.bcdt.ops.openapi.gitlab;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.gitlab.vi.GitlabHooksPullVi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "gitlab钩子" )
@RestController
public class GitlabHookCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "gitea钩子")
    @ACS(any = true)
    @PostMapping("/gitlab/gitHooks/push")
    public Void openApi(@Validated @RequestBody GitlabHooksPullVi req) {
        super.execute(GitlabHookService.class, req);
        return null;
    }
    
    
}
