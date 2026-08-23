package cc.bugcat.bcdt.ops.openapi.dbsql.list;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.dbsql.list.vi.DbsqlListVi;
import cc.bugcat.bcdt.ops.openapi.dbsql.list.vo.DbsqlListVo;
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
@Tag(name = "数据库版本" )
@RestController
public class DbsqlListCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "根据日期，查询sql列表")
    @PostMapping("/dbsql/list")
    @ACS(Opers.login)
    public ResultEntity<List<DbsqlListVo>> openApi(@Validated @RequestBody DbsqlListVi req) {
        return super.execute(DbsqlListService.class, req);
    }
    
    
}
