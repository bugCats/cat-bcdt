package cc.bugcat.bcdt.ops.openapi.chat.read;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.chat.read.vi.ChatReadVi;
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
@Tag(name = "消息中心" )
@RestController
public class ChatReadCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "异常信息标记已读")
    @PostMapping("/chat/msg/read")
    @ACS(Opers.login)
    public ResultEntity<Void> openApi(@Validated @RequestBody ChatReadVi req) {
        return super.execute(ChatReadService.class, req);
    }
    
    
    
}
