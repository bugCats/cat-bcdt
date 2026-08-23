package cc.bugcat.bcdt.ops.openapi.chat.count;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.chat.count.vi.ChatCountVi;
import cc.bugcat.bcdt.ops.openapi.chat.count.vo.ChatCountVo;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
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
@Tag(name = "消息中心" )
@RestController
public class ChatCountCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "个人待办列表")
    @PostMapping("/chat/msg/count")
    @ACS(Opers.login)
    public ResultEntity<List<ChatCountVo>> openApi(@Validated @RequestBody ChatCountVi req) {
        return super.execute(ChatCountService.class, req);
    }
    
    
}
