package cc.bugcat.bcdt.ops.openapi.dbEncry;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.dbEncry.vi.DbEncryptVi;
import cc.bugcat.bcdt.ops.openapi.dbEncry.vo.DbEncryptVo;
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
@Tag(name = "数据库加解密" )
@RestController
public class DbEncryptCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "数据库加解密")
    @PostMapping("/db/encry/coding")
    @ACS(Opers.login)
    public ResultEntity<List<DbEncryptVo>> openApi(@Validated @RequestBody DbEncryptVi req) {
        return super.execute(DbEncryptService.class, req);
    }
    
    
}