package cc.bugcat.bcdt.ops.openapi.dbauth.apply;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.dbauth.apply.vi.CodeApplyVi;
import cc.bugcat.bcdt.ops.openapi.dbauth.apply.vo.CodeApplyVo;
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
@Tag(name = "授权码" )
@RestController
public class CodeApplyCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "申请数据库授权码")
    @PostMapping("/auth/code/apply")
    @ACS(Opers.login)
    public ResultEntity<CodeApplyVo> openApi(@Validated @RequestBody CodeApplyVi req) {
        return super.execute(CodeApplyService.class, req);
    }
    
    
}
