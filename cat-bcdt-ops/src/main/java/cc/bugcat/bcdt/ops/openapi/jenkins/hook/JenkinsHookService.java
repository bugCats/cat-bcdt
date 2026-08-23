package cc.bugcat.bcdt.ops.openapi.jenkins.hook;


import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.constants.ParamConst;
import cc.bugcat.bcdt.ops.openapi.jenkins.hook.vi.JenkinsHookVi;
import cc.bugcat.bcdt.ops.service.inter.BcParamService;
import cc.bugcat.bcdt.ops.utils.ding.DingRobot;
import cc.bugcat.bcdt.ops.utils.ding.DingRobotMessage;
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
public class JenkinsHookService extends AbstractOpenApiService<JenkinsHookVi, Void> {
    
    private static Pattern jobEnvName = Pattern.compile("(dev|test|prod)([^-]*)\\-");
    
    @Autowired
    private BcParamService paramService;
    
    @Override
    public Void execute(JenkinsHookVi reqVi, RequestContext context) {
        
        String envCode = "";
        String env = reqVi.getJobName();
        Matcher matcher = jobEnvName.matcher(reqVi.getJobName());
        if(matcher.find()){
            envCode = matcher.group(1);
            env = envCode + matcher.group(2) + "环境";
        }
        String body = DingRobotMessage.getHeaderText(2, env) + "  " + DingRobotMessage.getBoldText(reqVi.getJobBaseName());
        String br = reqVi.getGitBranch().replace("origin/", "") + "分支";
        String mark = DateUtil.rs.format(new Date(), "HH:mm:ss") + " " + br + " " + StringUtils.trimToEmpty(reqVi.getBuildUser()) + " 正在更新......";
        String link = DingRobotMessage.getItalicText(DingRobotMessage.getLinkText("详情点击这里", reqVi.getBuildUrl() + "console"));

        String adminToken = paramService.getValue(ParamConst.ding.admin);
        String developerToken = paramService.getValue(ParamConst.ding.developer);

        String groupId = "prod".equals(envCode) ? adminToken: developerToken;
        DingRobot.build()
                .title("系统更新提醒")
                .body(body)
                .addContent(DingRobotMessage.getReferenceText(mark))
                .addContent(link)
                .token(groupId)
                .send();
        
        return null;
    }
}
