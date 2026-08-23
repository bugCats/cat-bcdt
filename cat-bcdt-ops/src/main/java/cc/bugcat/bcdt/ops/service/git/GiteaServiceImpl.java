package cc.bugcat.bcdt.ops.service.git;

import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.constants.ParamConst;
import cc.bugcat.bcdt.ops.model.zentao.BcTalk;
import cc.bugcat.bcdt.ops.model.zentao.ZtUser;
import cc.bugcat.bcdt.ops.service.inter.BcParamService;
import cc.bugcat.bcdt.ops.moduls.gitea.GitCheck;
import cc.bugcat.bcdt.ops.moduls.gitea.GitData;
import cc.bugcat.bcdt.ops.openapi.gitea.vi.*;
import cc.bugcat.bcdt.ops.service.inter.ZtUserService;
import cc.bugcat.bcdt.ops.service.inter.GiteaService;
import cc.bugcat.bcdt.ops.service.inter.BcTalkService;
import cc.bugcat.bcdt.ops.utils.ding.DingRobot;
import cc.bugcat.bcdt.ops.utils.ding.DingRobotMessage;
import cc.bugcat.bcdt.ops.web.Payloads;
import cc.bugcat.bcdt.ops.web.WebAction;
import cc.bugcat.bcdt.ops.web.WebTopic;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class GiteaServiceImpl implements GiteaService, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(GiteaServiceImpl.class);

    @Autowired
    private ZtUserService ztUserService;
    
    @Autowired
    private BcTalkService talkService;
    
    @Autowired
    private BcParamService paramService;

    private String configPath;  //配置文件地址
    private GitData gitData;

    @Override
    public void afterPropertiesSet() throws Exception {
        String tomcatHome = System.getProperty("catalina.base");
        File giteaDir = new File(tomcatHome + "/data/gitea/");
        if( !giteaDir.exists() ){
            giteaDir.mkdirs();
        }
        this.configPath = giteaDir.getPath() + "/conf.json";
        File config = new File(configPath);
        if( !config.exists() ){
            config.createNewFile();
        }
        String json = FileUtils.readFileToString(config, "utf-8");
        gitData = JsonTools.parseObject(json, GitData.class);
        if ( gitData == null ) {
            gitData = new GitData();
            gitData.setCheck(new TreeMap<>());
        }
    }



    @Override
    public GitData queryFiles() {
        return gitData;
    }

    @Override
    public GitData saveFiles(GitData vi) {
        vi.getCheck().forEach((name, check) -> {
            Set<String> fileNames = check.getFileName();
            TreeSet<String> files = new TreeSet<>();
            for ( String fileName : fileNames ) {
                fileName = fileName.replace("\\", "/");
                if( fileName.startsWith("/") ){
                    fileName = fileName.substring(1);
                }
                files.add(fileName);
            }
            check.setFileName(files);
        });
        this.gitData = vi;
        File config = new File(configPath);
        try { FileUtils.write(config, JsonTools.toJson(vi), "utf-8", false); } catch ( Exception e ) { }
        return vi;
    }
    
    
    /**
     * git 推送事件
     * */
    @Override
    public void push(String reqStr) {
        GiteaHooksPush vi = JsonTools.parseObject(reqStr, GiteaHooksPush.class);

        String repoName = vi.getRepository().getName(); //仓库名称
        String branch = StringUtils.substringAfterLast(vi.getRef(), "/"); //分支名称

        GitCheck gitCheck = gitData.getCheck().get(repoName); //配置的关键文件
        if( gitCheck == null ){
            return;
        }
        //提交人：提交信息
        Map<String, Committer> committerMap = new HashMap<>();

        for ( GiteaHooksPush.PushCommit commit : vi.getCommits() ) { //提交记录
            List<String> modified = commit.getModified();
            List<String> removed = commit.getRemoved();
            List<String> checkFiles = new ArrayList<>(gitCheck.getFileName().size());
            checkFiles.addAll(checkFile(gitCheck, modified));
            checkFiles.addAll(checkFile(gitCheck, removed));
            GiteaHooksPush.CommitAuthor author = commit.getAuthor();
            Committer committer = new Committer(author.getName(), checkFiles);
            committerMap.put(committer.name, committer);
        }

        List<String> list = new ArrayList<>();
        String format = "%s提交了%s@%s分支：%s";
        committerMap.forEach((name, committer) -> {
            List<String> commits = committer.commits;
            for (int idx = 0; idx < commits.size(); idx ++ ) {
                String cm = (idx + 1) + ". " + String.format(format, name, repoName, branch, commits.get(idx));
                list.add(cm);
            }
        });
        if( ObjectUtil.isNotEmpty(list) ){
            String adminToken = paramService.getValue(ParamConst.ding.admin);
            DingRobot.build()
                    .title("代码异常提交警告")
                    .body(vi.getPusher().getFullName() + "修改了风险项文件")
                    .content(list)
                    .token(adminToken)
                    .send();
        }
    }


    /**
     * 合并请求事件
     * */
    @Override
    public void pullRequest(String reqStr) {
        GiteaHooksPull vi = JsonTools.parseObject(reqStr, GiteaHooksPull.class);

        List<UserEntry> toUserList = new ArrayList<>();	//多个使用英文逗号隔开
        GiteaHooksPull.GitPullRequest pullRequest = vi.getPullRequest();
        GiteaUser user = pullRequest.getUser();

        String actionName = "";

        String action = vi.getAction().toLowerCase();	//opened edited closed
        String actions = "," + action + ",";
        if( ",opened,reopened,assigned,".contains(actions) ){ //新开、指派，发给审核人

            GiteaUser assignee = pullRequest.getAssignee();
            if( assignee == null ){
                return;
            }
            UserEntry toUser = findZtUserByUser(assignee);
            if( toUser == null ){
                return;
            }
            toUserList.add(toUser);
            actionName = "您有合并请求待处理";

        } else if( "closed".equals(action) ){	//被关闭，通知申请人
            UserEntry toUser = findZtUserByUser(user);		//
            if( toUser == null ){
                return;
            }
            toUserList.add(toUser);
            
            GiteaUser merger = pullRequest.getMergedBy();
            if( merger != null ){
                actionName = "您提交的合并请求已通过";
            } else {
                actionName = "您提交的合并请求被拒绝";
            }
            
        } else if ("edited".equals(action)){	//被编辑，发给申请人、审核人

            String username = vi.getSender().getUsername();	//评论人

            GiteaUser assignee = pullRequest.getAssignee();	//审核人
            if( !username.equalsIgnoreCase(assignee.getUsername()) ){
                UserEntry toUser = findZtUserByUser(assignee);
                if ( toUser != null ) {
                    toUserList.add(toUser);
                }
            }
            if( !username.equalsIgnoreCase(user.getUsername()) ){
                UserEntry toUser = findZtUserByUser(user);
                if ( toUser != null ) {
                    toUserList.add(toUser);
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
                talk.setSendAccount(user.getUsername());
                talk.setSendRealname(user.getFullName());
                talk.setContent(BcTalkService.markText(actionName, "title") + BcTalkService.markText(pullRequest.getTitle(), "comment") + BcTalkService.linkText("详情点击这里", pullRequest.getHtmlUrl()));
                talk.setMsgType(BcTalk.TextType.git.code);
                talk.setRead("0");
                talk.setSaveDate(new Date());
                talkService.save(talk);

                JsonTools payload = Payloads.notifyBuilder()
                        .title("【Gitea】" + actionName)
                        .content(pullRequest.getTitle())
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


    /**
     * 评论
     * */
    @Override
    public void issueComment(String reqStr) {
        GiteaHooksComment vi = JsonTools.parseObject(reqStr, GiteaHooksComment.class);
        
        //合并地址、申请人、审核人
        GiteaHooksComment.GitIssue issue = vi.getIssue();
        
        //评论信息：评论人、评论内容
        GiteaHooksComment.GitComment comment = vi.getComment();
        
        GiteaUser commentUuser = comment.getUser();	//评论人

        GiteaUser assignee = issue.getAssignee();	//审核人
        GiteaUser user = issue.getUser();

        List<UserEntry> toUserList = new ArrayList<>();	//多个使用英文逗号隔开
        
        if( !commentUuser.getUsername().equalsIgnoreCase(assignee.getUsername()) ){
            UserEntry toUser = findZtUserByUser(assignee);
            if ( toUser != null ) {
                toUserList.add(toUser);
            }
        }
        if( !commentUuser.getUsername().equalsIgnoreCase(user.getUsername()) ){
            UserEntry toUser = findZtUserByUser(user);
            if ( toUser != null ) {
                toUserList.add(toUser);
            }
        }
        if( ObjectUtil.isEmpty(toUserList) ){
            return;
        }
        
        String actionName = "您提交的合并请求被编辑";
        for ( UserEntry entry : toUserList ) {
            try {
                BcTalk talk = new BcTalk();
                talk.setAccount(entry.username);
                talk.setRealname(entry.realname);
                talk.setSendAccount(user.getUsername());
                talk.setSendRealname(user.getFullName());
                talk.setContent(BcTalkService.markText(actionName, "title") + BcTalkService.markText(comment.getBody(), "comment") + BcTalkService.linkText("详情点击这里", issue.getHtmlUrl()));
                talk.setMsgType(BcTalk.TextType.git.code);
                talk.setRead("0");
                talk.setSaveDate(new Date());
                talkService.save(talk);

                JsonTools payload = Payloads.notifyBuilder()
                        .title("【Gitea】" + actionName)
                        .content(comment.getBody())
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

    
    
    /**
     * 创建分支、标签事件
     * */
    @Override
    public void create(String reqStr) {
        GiteaHooksCreate vi = JsonTools.parseObject(reqStr, GiteaHooksCreate.class);
        if( "tag".equals(vi.getRefType()) ){
            try {
                String title = "Git标签推送提醒";
                String body = vi.getSender().getFullName() + "推送了[" + vi.getRepository().getName() + "]仓库新标签[" + vi.getRef() + "]";
                String comment = DingRobotMessage.getItalicText(DateUtil.ll.getDate());
                // 
            } catch ( Exception ex ) {
                ex.printStackTrace();
            }
        }
    }





    private UserEntry findZtUserByUser(GiteaUser user){
        if( user == null ){
            return null;
        }
        ZtUser emp = ztUserService.findGitByAccount(user.getFullName(), user.getUsername());
        if ( emp == null ) {
            return null;
        }
        return new UserEntry(emp.getAccount(), emp.getRealname());
    }


    
    /**
     * 检查文件
     * */
    private List<String> checkFile(GitCheck gitCheck, List<String> files){
        final Set<String> checks = gitCheck.getFileName();
        List<String> list = new ArrayList<>(checks.size());
        if( gitCheck == null || ObjectUtil.isEmpty(files) ){
            return list;
        }
        for(String file : files){
            String fileName = null;
            if( file.contains("/src/main/java") ){ 
                // java后台
                fileName = StringUtils.substringAfter(file, "/src/main/java");
                
            } else if( file.contains("/src/main/three") ){ 
                // java后台
                fileName = StringUtils.substringAfter(file, "/src/main/three");
                
            } else if( file.contains("/src/main/resources") ){ 
                // java后台配置文件
                fileName = StringUtils.substringAfter(file, "/src/main/resources");
                
            } else {
                // 其他web项目
                fileName = file;
                
            }
            
            if( checks.contains(fileName) ){ // java 包
                list.add(fileName);
            }
        }
        return list;
    }


    
    
    /**
     * 提交人
     * xxxx提交了tmc-web master分支 xxxx文件
     * */
    private record Committer(String name, List<String> commits) {}

    
    private record UserEntry(String username, String realname) {}
    
}
