package cc.bugcat.bcdt.ops.openapi.dbauth.apply;

import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.acore.framework.utils.MdEncrypt;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.acore.framework.utils.WorkPool;
import cc.bugcat.acore.webmvc.utils.ServletUtil;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.config.AppConfig;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.constants.ParamConst;
import cc.bugcat.bcdt.ops.moduls.dbauth.DatabaseAuth;
import cc.bugcat.bcdt.ops.openapi.dbauth.adopt.CodeAdoptService;
import cc.bugcat.bcdt.ops.openapi.dbauth.adopt.vi.CodeAdoptVi;
import cc.bugcat.bcdt.ops.openapi.dbauth.apply.vi.CodeApplyVi;
import cc.bugcat.bcdt.ops.openapi.dbauth.apply.vo.CodeApplyVo;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.service.inter.BcParamService;
import cc.bugcat.bcdt.ops.service.mysql.spi.IUserAuthCacheService;
import cc.bugcat.bcdt.ops.utils.CodeUtil;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import cc.bugcat.bcdt.ops.utils.ding.DingRobot;
import cc.bugcat.bcdt.ops.utils.ding.DingRobotMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class CodeApplyService extends AbstractOpenApiService<CodeApplyVi, CodeApplyVo> {
    
    @Autowired
    private IUserAuthCacheService userAuthCacheService;
    
    @Autowired
    private CodeAdoptService codeAdoptService;
    
    @Autowired
    private AppConfig appConfig;
    
    @Autowired
    private BcParamService paramService;
    
    
    @Override
    public CodeApplyVo execute(CodeApplyVi reqVi, RequestContext context) {
        
        UserInfo user = UserUtil.getUser(context);
        
        //登录人账户
        final String account = user.getAccount();
        //子账号
        final String userCode = ("1".equals(reqVi.getWrite()) ? "w" : "r") + CodeUtil.userCode(account, user.getRealname());
        //授权码
        final String authCode = CodeUtil.authCode();
        
        //子账号密码
        final String userPwd = MdEncrypt.md5(CodeUtil.userCode(account, userCode));
        final String realUserCode = account + "@" + userCode;
        final String realAuthCode = account + "#" + authCode;
        
        DatabaseAuth userAuth = userAuthCacheService.codeApply(realUserCode, auth -> {
            auth.setRealname(user.getRealname());
            auth.setReason(reqVi.getReason());
            auth.setIpAddr(ServletUtil.getIpAddr(context.getRequest()));
            auth.setAuthCode(realAuthCode); //实际缓存的授权码
            auth.setAccount(account);       //登录人账号
            auth.setUserCode(realUserCode); //子账号 username@code
            auth.setUserPwd(userPwd);   //子账号密码
            auth.setWrite(reqVi.getWrite());
            auth.setDba(Opers.hasOper(user, Opers.dba));
        });
        
        //发送授权码
        this.applyAuthorize(userAuth, user);
        
        CodeApplyVo authVo = new CodeApplyVo();
        authVo.setUserCode(realUserCode);
        authVo.setUserPwd(userPwd);
        
        return authVo;
    }
    
    
    /**
     * 申请数据库授权
     *
     * @param userAuth
     * @param user
     * */
    private void applyAuthorize(DatabaseAuth userAuth, UserInfo user) {
        
        String authCode = StringUtils.substringAfter(userAuth.getAuthCode(), "#");
        String nowTime = DateUtil.ll.getDate();
        
        DingRobot robot = DingRobot.build()
                .title("数据库申请授权")
                .addContent(DingRobotMessage.getReferenceText(userAuth.getReason()))
                .addContent(DingRobotMessage.getItalicText("申请IP:" + userAuth.getIpAddr() + "  [" + nowTime + "]"))
                .addContent(DingRobotMessage.getItalicText("授权码:" + authCode));
        
        String body = "  " + DingRobotMessage.getBoldText("正在申请数据库授权");
        
        // 1读写群，0只读群：
        if( "1".equals(userAuth.getWrite()) ){
            String writeToken = paramService.getValue(ParamConst.ding.dbAuthWrite);
            robot.token(writeToken)
                    .body(DingRobotMessage.getHeaderText(2, user.getRealname()) + body);
            
        } else {
            String readToken = paramService.getValue(ParamConst.ding.dbAuthRead);
            robot.token(readToken)
                    .body(DingRobotMessage.getHeaderText(2, "@" + StringUtils.defaultIfBlank(user.getPhone(), user.getRealname())) + body)
                    .addAtPhone(user.getPhone());
            
        }
        
        // todo
        // 标准流程是授权码发送到钉钉群，然后在钉钉聊天窗口点击‘转发’完成同意授权。
        // 开发阶段没有对接钉钉，采用延时5s后，自动同意；
        WorkPool.delay(() -> {
            CodeAdoptVi req = new CodeAdoptVi();
            req.setAccount(userAuth.getAccount());
            req.setCode(authCode);
            codeAdoptService.execute(req, null);
        }, 5000L);
        
        
        String callbackUrl = appConfig.getBcdtOpsHost() + "/auth/code/adopt?account=" + userAuth.getAccount() + "&code=" + authCode;
        robot.send(ObjectUtil.asList(new String[]{"转发", callbackUrl}));
        
    }
    
}

