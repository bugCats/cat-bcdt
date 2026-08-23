package cc.bugcat.bcdt.ops.openapi.zentao.all;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.framework.entity.PageInfo;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.zentao.all.vi.ZenTaoTaskVi;
import cc.bugcat.bcdt.ops.openapi.zentao.all.vo.ZenTaoAllTaskVo;
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
public class ZenTaoAllTaskCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "查询禅道任务列表")
    @PostMapping("/zentao/all/list")
    @ACS(Opers.login)
    public ResultEntity<PageInfo<ZenTaoAllTaskVo>> openApi(@Validated @RequestBody ZenTaoTaskVi req) {
        return super.execute(ZenTaoAllTaskService.class, req);
    }
    
}
