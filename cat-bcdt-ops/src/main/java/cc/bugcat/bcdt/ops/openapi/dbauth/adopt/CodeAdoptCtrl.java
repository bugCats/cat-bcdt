package cc.bugcat.bcdt.ops.openapi.dbauth.adopt;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.dbauth.adopt.vi.CodeAdoptVi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "授权码" )
@RestController
public class CodeAdoptCtrl extends AbstractOpenApiCtrl {
    
    
    @Operation(summary = "钉钉同意转发授权码")
    @GetMapping("/auth/code/adopt")
    @ACS(any = true)
    public String openApi(@Validated @ModelAttribute CodeAdoptVi req) {
        String resp = super.process(CodeAdoptService.class, req);
        return resp;
    }
    
}
