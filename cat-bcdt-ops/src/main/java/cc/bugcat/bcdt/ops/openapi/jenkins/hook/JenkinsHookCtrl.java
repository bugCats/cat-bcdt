package cc.bugcat.bcdt.ops.openapi.jenkins.hook;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.jenkins.hook.vi.JenkinsHookVi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "Jenkins 钩子" )
@RestController
public class JenkinsHookCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "Jenkins钩子")
    @ACS(any = true)
    @PostMapping("/jenkins/build/hooks")
    public Void openApi(@Validated @ModelAttribute JenkinsHookVi req) {
        super.execute(JenkinsHookService.class, req);
        return null;
    }
    
    
    
}
