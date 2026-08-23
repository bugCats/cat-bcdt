package cc.bugcat.bcdt.ops.openapi.zentao.list;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.zentao.list.vi.ZenTaoListVi;
import cc.bugcat.bcdt.ops.openapi.zentao.list.vo.ZenTaoListVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "禅道" )
@RestController
public class ZenTaoListCtrl extends AbstractOpenApiCtrl {
    
    
    @Operation(summary = "根据id查询禅道任务")
    @PostMapping("/zentao/list")
    @ACS(Opers.login)
    public ResultEntity<List<ZenTaoListVo>> openApi(@Validated @RequestBody ZenTaoListVi req) {
        return super.execute(ZenTaoListService.class, req);
    }

}
