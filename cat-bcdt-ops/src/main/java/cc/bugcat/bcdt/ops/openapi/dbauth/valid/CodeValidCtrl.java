package cc.bugcat.bcdt.ops.openapi.dbauth.valid;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "授权码" )
@RestController
public class CodeValidCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "校验授权码")
    @PostMapping("/auth/code/valid")
    @ACS(Opers.login)
    public ResultEntity<Void> openApi(@RequestParam("code") String req) {
        return super.execute(CodeValidService.class, req);
    }
    
    
}
