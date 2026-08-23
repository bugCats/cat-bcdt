package cc.bugcat.bcdt.ops.openapi.chat.list;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.chat.list.vi.ChatListVi;
import cc.bugcat.bcdt.ops.openapi.chat.list.vo.ChatListVo;
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
public class ChatListCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "个人待办列表")
    @PostMapping("/chat/msg/list")
    @ACS(Opers.login)
    public ResultEntity<List<ChatListVo>> openApi(@Validated @RequestBody ChatListVi req) {
        return super.execute(ChatListService.class, req);
    }
    
    
}
