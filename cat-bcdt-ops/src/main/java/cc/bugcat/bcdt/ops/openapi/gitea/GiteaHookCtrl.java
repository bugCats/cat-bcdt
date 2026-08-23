package cc.bugcat.bcdt.ops.openapi.gitea;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
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
@Tag(name = "gitea钩子" )
@RestController
public class GiteaHookCtrl extends AbstractOpenApiCtrl {
    
    
    @Operation(summary = "gitea钩子")
    @ACS(any = true)
    @PostMapping("/gitea/gitHooks/push")
    public Void openApi(@Validated @RequestBody String req) {
        super.execute(GiteaHookService.class, req);
        return null;
    }
    
    
}
