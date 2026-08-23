package cc.bugcat.bcdt.ops.openapi.zentao.hook;

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
@Tag(name = "禅道" )
@RestController
public class ZenTaoHookCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "禅道触发钩子")
    @PostMapping("/zentao/webhook")
    @ACS(any = true)
    public String openApi(@Validated @RequestBody String req) {
        return super.process(ZenTaoHookService.class, req);
    }
    
}
