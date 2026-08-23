package cc.bugcat.bcdt.ops.openapi.dbauth.adopt;


import cc.bugcat.acore.framework.cache.spi.InnerCache;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.model.zentao.ZtUser;
import cc.bugcat.bcdt.ops.moduls.dbauth.DatabaseAuth;
import cc.bugcat.bcdt.ops.openapi.dbauth.adopt.vi.CodeAdoptVi;
import cc.bugcat.bcdt.ops.service.inter.ZtUserService;
import cc.bugcat.bcdt.ops.web.WebAction;
import cc.bugcat.bcdt.ops.web.WebTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class CodeAdoptService extends AbstractOpenApiService<CodeAdoptVi, String> {
    
    
    @Autowired
    private ZtUserService ztUserService;

    @Autowired
    private InnerCache cache;
    
    
    @Override
    public String execute(CodeAdoptVi reqVi, RequestContext context) {
        String reason = null;
        try {
            reason = this.publishAuthorize(reqVi.getAccount(), reqVi.getCode());
        } catch ( Exception ex ) {
            reason = "<h3>转发异常</h3><div>" + ex.getMessage() + "</div>";
        }
        return reason;
    }
    
    
    
    /**
     * 转发、发布授权码
     * */
    private String publishAuthorize(String account, String code) throws Exception {
        
        ZtUser ztUser = ztUserService.findByAccount(account);
        if( ztUser == null ){
            throw new Exception("未知的账号[" + account + "]！");
        }
        
        String authCode = account + "#" + code;
        DatabaseAuth userAuth = cache.get(authCode, DatabaseAuth.class);
        if ( userAuth == null ) {
            throw new Exception("授权码已失效，请重新授权！");
        }
        
        // ws直接将授权码填充到页面
        WebTopic.builder()
                .webSessions(account)
                .action(WebAction.db_auth)
                .notify(false)
                .payload(code)
                .doSend();
        
        return "<h3>已转发</h3>";
    }
}
