package cc.bugcat.bcdt.ops.service.git;

import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.acore.http.HttpUtil;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.config.GitConfig;
import cc.bugcat.bcdt.ops.model.zentao.BcTalk;
import cc.bugcat.bcdt.ops.model.zentao.ZtUser;
import cc.bugcat.bcdt.ops.openapi.gitlab.vi.GitlabHooksPullVi;
import cc.bugcat.bcdt.ops.service.inter.ZtUserService;
import cc.bugcat.bcdt.ops.service.inter.BcTalkService;
import cc.bugcat.bcdt.ops.service.inter.GitlabService;
import cc.bugcat.bcdt.ops.web.Payloads;
import cc.bugcat.bcdt.ops.web.WebAction;
import cc.bugcat.bcdt.ops.web.WebTopic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class GitlabServiceImpl implements GitlabService {

    private final Logger logger = LoggerFactory.getLogger(GitlabServiceImpl.class);
    
    @Autowired
    private GitConfig gitConfig;

    @Autowired
    private ZtUserService ztUserService;

    @Autowired
    private BcTalkService talkService;
    
    @Override
    public void pullRequest(GitlabHooksPullVi vi) {
        
        GitlabHooksPullVi.ObjectAttributes attrs = vi.getObjectAttributes();

        List<UserEntry> toUserList = new ArrayList<>();	//多个使用英文逗号隔开

        String actionName = "";
        
        /**
         * 触发事件
         * open reopen merge close update approved unapproved pproval unapproval
         * */
        String action = attrs.getAction().toLowerCase();
        String actions = "," + action + ",";
        
        if( ",open,reopen,".contains(actions) ){ //新开，给指派人发消息

            String assigneeId = attrs.getAssigneeId();	//指派人id
            if( StringUtils.isBlank(assigneeId) ){
                return; // 没有指派人
            }
            UserEntry toUser = queryUsernameById(assigneeId);
            if( toUser == null ){
                return;
            }
            toUserList.add(toUser);

            actionName = "您有合并请求待处理";

        } else if ( ",merge,close,".contains(actions) ) { //合并、关闭，发给发起人
            String authorId = attrs.getAuthorId();
            UserEntry toUser = queryUsernameById(authorId);
            if( toUser == null ){
                return;
            }
            toUserList.add(toUser);
            
            if( "merge".equals(action) ){
                actionName = "您提交的合并请求已通过";
            } else {
                actionName = "您提交的合并请求被拒绝";
            }

        } else if (",update,approved,unapproved,pproval,unapproval,".contains(actions)){	//被编辑，发给申请人、审核人
            
            String authorId = attrs.getAuthorId();  //合并请求发起人
            String assigneeId = attrs.getAssigneeId();	//指派人id
            String user = vi.getUser().getId();//更新人
            
            if( StringUtils.isNotBlank(authorId) && !authorId.equals(user) ){
                UserEntry author = queryUsernameById(authorId);
                if( author != null ){
                    toUserList.add(author);
                }
            }
            if( StringUtils.isNotBlank(assigneeId) && !assigneeId.equals(user) ){
                UserEntry assignee = queryUsernameById(assigneeId);
                if( assignee != null ){
                    toUserList.add(assignee);
                }
            }
            actionName = "您提交的合并请求被编辑";

        } else {
            return;
        }

        if( ObjectUtil.isEmpty(toUserList) ){
            return;
        }
        
        for ( UserEntry entry : toUserList ) {
            try {
                BcTalk talk = new BcTalk();
                talk.setAccount(entry.username);
                talk.setRealname(entry.realname);
                talk.setSendAccount("Gitlab");
                talk.setSendRealname("Gitlab");
                talk.setContent(BcTalkService.markText(actionName, "title") + BcTalkService.markText(attrs.getTitle(), "comment"));
                talk.setMsgType(BcTalk.TextType.git.code);
                talk.setRead("0");
                talk.setSaveDate(new Date());
                talkService.save(talk);

                JsonTools payload = Payloads.notifyBuilder()
                        .title("【Gitlab】" + actionName)
                        .content(attrs.getTitle())
                        .msgType(talk.getMsgType())
                        .build();
                WebTopic.builder()
                        .webSessions(talk.getAccount())
                        .action(WebAction.web_alert)
                        .payload(payload)
                        .notify("0".equals(talk.getRead()))
                        .doSend();
            } catch ( Exception ex ) {
                logger.error("保存消息异常：{}", BusinessException.getStackTrace(ex));
            }
        }
    }
    
    
    private UserEntry queryUsernameById(String uid){
        try {
            String path = gitConfig.getHost() + "/api/v4/users?access_token=" + gitConfig.getToken() + "&id=" + uid;
            String resp = HttpUtil.creart().doGet().send(path);
            List<JsonTools> users = JsonTools.parseArray(resp);
            if( ObjectUtil.isNotEmpty(users) ){
                JsonTools user = users.getFirst();
                String username = user.getString("username");
                String name = user.getString("name");
                ZtUser emp = ztUserService.findGitByAccount(name, username);
                if( emp != null ){
                    return new UserEntry(emp.getAccount(), emp.getRealname());
                }
            }
        } catch ( Exception ex ) {
            logger.error(BusinessException.getStackTrace(ex));
        }
        return null;
    }


    private record UserEntry(String username, String realname) {}
}
