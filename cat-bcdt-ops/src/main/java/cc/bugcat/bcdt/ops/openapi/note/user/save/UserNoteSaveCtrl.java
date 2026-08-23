package cc.bugcat.bcdt.ops.openapi.note.user.save;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.note.user.save.vi.UserNoteSaveVi;
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
@Tag(name = "日报" )
@RestController
public class UserNoteSaveCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "保存日报")
    @PostMapping("/user/save/note")
    @ACS(Opers.login)
    public ResultEntity<Void> openApi(@Validated @RequestBody UserNoteSaveVi req) {
        return super.execute(UserNoteSaveService.class, req);
    }
    
    
}
