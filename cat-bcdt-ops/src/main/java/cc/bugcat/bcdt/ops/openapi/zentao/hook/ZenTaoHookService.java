package cc.bugcat.bcdt.ops.openapi.zentao.hook;

import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.acore.framework.utils.UnicodeUtil;
import cc.bugcat.acore.framework.utils.WorkPool;
import cc.bugcat.bcdt.ops.constants.ParamConst;
import cc.bugcat.bcdt.ops.model.zentao.BcTalk;
import cc.bugcat.bcdt.ops.model.zentao.ZtUser;
import cc.bugcat.bcdt.ops.service.inter.BcParamService;
import cc.bugcat.bcdt.ops.openapi.zentao.hook.vi.ZenTaoHookVi;
import cc.bugcat.bcdt.ops.service.inter.BcTalkService;
import cc.bugcat.bcdt.ops.service.inter.ZenTaoService;
import cc.bugcat.bcdt.ops.service.inter.ZtUserService;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.utils.ding.DingRobot;
import cc.bugcat.bcdt.ops.web.Payloads;
import cc.bugcat.bcdt.ops.web.WebAction;
import cc.bugcat.bcdt.ops.web.WebTopic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class ZenTaoHookService extends AbstractOpenApiService<String, String> {
    
    //webhook默认返回
    private static final String RESP = "{\"errmsg\":\"ok\",\"errcode\":0}";
    
    //超链接
    private static final Pattern linkPat = Pattern.compile("([\\s\\S]+)\\[([\\s\\S]+)\\]\\(([\\s\\S]+)\\)");
    
    //替换禅道描述中图片引用
    private static final Pattern imgPat = Pattern.compile("(\\!\\[image\\]\\([\\s\\S]+\\))");
    
    //空行
    private static final Pattern emptyLine1 = Pattern.compile("(\\s*<br\\s*/?>\\s*)");
    private static final Pattern emptyLine2 = Pattern.compile("(\\s*<p>\\s*</p>)");
    
    
    @Autowired
    private ZenTaoService zenTaoService;
    
    @Autowired
    private ZtUserService ztUserService;
    
    @Autowired
    private BcTalkService talkService;
    
    @Autowired
    private BcParamService paramService;
    
    
    
    @Override
    public String execute(String reqVi, RequestContext context) {
        
        WorkPool.createThread(() -> {
            
            String req = UnicodeUtil.decrypt(reqVi);
            ZenTaoHookVi data = JsonTools.parseObject(req, ZenTaoHookVi.class);
            
            // account：被指派人，= closed 表示已经关闭
            String account = zenTaoService.findAccountByObjectId(data.getObjectType(), data.getObjectID());
            if( StringUtils.isBlank(account) || "closed".equals(account) ){
                return;
            }
            
            //消息接收人，禅道用户
            ZtUser ztUser = ztUserService.findByAccount(account);
            if( ztUser == null ){
                userNotfount(account, "102");
                return;
            }
            
            //消息发送人
            ZtUser actor = ztUserService.findByAccount(data.getActor());
            String actorName = StringUtils.defaultIfBlank(actor.getRealname(), data.getActor());
            long time = System.currentTimeMillis();
            
            String title = "禅道消息";
            String dataText = data.getText().replace(data.getActor(), actorName);
            String dataComment = imgPat.matcher(data.getComment()).replaceAll("");
            
            Matcher linkMat = linkPat.matcher(dataText);
            
            String talkTitle = linkMat.replaceAll("$1<a href='$3@time@' target='_blank'><span class='link'>$2</span></a>").replace("@time@", "?_=" + time);
            dataComment = dataComment.replace("\\r\\n", "");
            dataComment = emptyLine1.matcher(dataComment).replaceAll("");
            dataComment = emptyLine2.matcher(dataComment).replaceAll("");
            String talkComment = dataComment;
            BcTalk talk = new BcTalk();
            talk.setAccount(ztUser.getAccount());
            talk.setRealname(ztUser.getRealname());
            talk.setSendAccount(actor.getAccount());
            talk.setSendRealname(actor.getRealname());
            talk.setContent(BcTalkService.markText(talkTitle, "title") + BcTalkService.markText(talkComment, "comment"));
            talk.setMsgType(BcTalk.TextType.zen_tao.code);
            talk.setRead("0");
            talk.setSaveDate(new Date());
            talkService.save(talk);

            JsonTools payload = Payloads.notifyBuilder()
                    .title("【禅道】" + title)
                    .content(StringUtils.substringBefore(dataText, "[") + " #" + data.getObjectID())
                    .msgType(talk.getMsgType())
                    .build();
            
            WebTopic.builder()
                    .webSessions(talk.getAccount())
                    .action(WebAction.web_alert)
                    .payload(payload)
                    .notify("0".equals(talk.getRead()))
                    .doSend();
        });
        
        return RESP;
        
    }
    
    
    
    
    
    private void userNotfount(String account, String errCode){
        String errMsg = "";
        if( "102".equals(errCode) ){
            errMsg = "未知的禅道账号[" + account + "]";
        } else if ( "103".equals(errCode) ) {
            errMsg = "未知的禅道账号[" + account + "]，请确保禅道上的手机号、真实姓名，至少有一项与钉钉工号、钉钉姓名保持一致！";
        } else if ( "104".equals(errCode) ) {
            errMsg = "未匹配的禅道账号[" + account + "]";
        }
        String developerToken = paramService.getValue(ParamConst.ding.developer);
        DingRobot robot = DingRobot.build()
                .title("未知禅道账号异常提醒")
                .token(developerToken)
                .body(errMsg);
        robot.send();
    }
    
}
