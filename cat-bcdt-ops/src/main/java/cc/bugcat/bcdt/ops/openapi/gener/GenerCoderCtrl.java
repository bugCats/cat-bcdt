package cc.bugcat.bcdt.ops.openapi.gener;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.gener.vi.GenerCoderVi;
import cc.bugcat.bcdt.ops.openapi.gener.vo.GenerCoderVo;
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
@Tag(name = "代码生成" )
@RestController
public class GenerCoderCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "代码生成")
    @PostMapping("/api/gencoder")
    @ACS(any = true)
    public ResultEntity<GenerCoderVo> openApi(@Validated @RequestBody GenerCoderVi req) {
        return super.execute(GenerCoderService.class, req);
    }
    
    
    
}
