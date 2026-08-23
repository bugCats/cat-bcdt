package cc.bugcat.bcdt.ops.openapi.dbauth.info;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.dbauth.info.vi.CodeInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "授权码" )
@RestController
public class CodeInfoCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "我的授权码")
    @GetMapping("/auth/code/info")
    @ACS(Opers.login)
    public ResultEntity<CodeInfoVo> openApi() {
        return super.execute(CodeInfoService.class, null);
    }
    
    
}
