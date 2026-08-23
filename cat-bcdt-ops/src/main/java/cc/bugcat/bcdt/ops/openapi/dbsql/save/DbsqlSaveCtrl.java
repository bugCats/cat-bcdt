package cc.bugcat.bcdt.ops.openapi.dbsql.save;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.dbsql.save.vi.DbsqlSaveVi;
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
@Tag(name = "数据库版本" )
@RestController
public class DbsqlSaveCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "保存sql")
    @PostMapping("/dbsql/save")
    @ACS(Opers.login)
    public ResultEntity<Void> openApi(@Validated @RequestBody DbsqlSaveVi req) {
        return super.execute(DbsqlSaveService.class, req);
    }
    
    
}
