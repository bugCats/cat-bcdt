package cc.bugcat.bcdt.ops.openapi.common;


import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.bcdt.ops.web.WebAction;
import cc.bugcat.bcdt.ops.web.WebTopic;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 给指定账号发送websocket消息
 * 
 * @author bugcat-cc
 */
@RestController
public class WebsktCtrl {
    
    
    
    @ACS(any = true)
    @PostMapping("/ws/send")
    public ResultEntity<Void> send(@RequestBody JsonTools req){
        
        String account = req.getString("account");
        String action = req.getString("action");
        String notify = req.getString("notify");
        Object body = req.get("body");
        
        WebTopic.builder()
                .webSessions(account)
                .action(WebAction.valueOf(action))
                .notify("1".equals(notify))
                .payload(body)
                .doSend();
        
        return ResultEntity.ok();
    }
    
    
}
