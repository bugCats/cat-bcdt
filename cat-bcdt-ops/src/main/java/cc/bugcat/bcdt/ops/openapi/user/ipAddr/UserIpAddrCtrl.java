package cc.bugcat.bcdt.ops.openapi.user.ipAddr;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.user.ipAddr.vo.UserIpAddrVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "用户" )
@RestController
public class UserIpAddrCtrl extends AbstractOpenApiCtrl {
    
    

    @Operation(summary = "用户IP")
    @GetMapping("/user/ip/list")
    @ACS(Opers.login)
    public ResultEntity<List<UserIpAddrVo>> openApi(){
        return super.execute(UserIpAddrService.class, null);
    }
    
}
