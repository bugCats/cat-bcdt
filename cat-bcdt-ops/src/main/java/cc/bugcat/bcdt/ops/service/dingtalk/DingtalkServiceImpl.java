package cc.bugcat.bcdt.ops.service.dingtalk;


import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.acore.http.HttpUtil;
import cc.bugcat.bcdt.ops.config.AppConfig;
import cc.bugcat.bcdt.ops.constants.ParamConst;
import cc.bugcat.bcdt.ops.service.inter.BcParamService;
import cc.bugcat.bcdt.ops.service.inter.DingtalkService;
import cc.bugcat.bcdt.ops.utils.ding.DingRobot;
import cc.bugcat.bcdt.ops.utils.ding.DingRobotMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 
 * 钉钉群发：
 * @see DingRobot
 * @author bugcat-cc
 */
@Service("dingtalkService")
public class DingtalkServiceImpl implements DingtalkService, InitializingBean {

    //截取图片标签
    private static final Pattern imgPattern = Pattern.compile("(<img\\s+src=.+?\\{(.+?)\\}.*?>)");


    //token缓存
    private TokenCache tokenCache;

    @Autowired
    private AppConfig appConfig;
    
    @Autowired
    private BcParamService paramService;
    
    
    
    @Override
    public void afterPropertiesSet() {
        tokenCache = new TokenCache();
    }


    /**
     * 获取token
     * */
    private String dingtalkGetToken() {
        final String appKey = paramService.getValue(ParamConst.ding.appKey);
        final String appsecret = paramService.getValue(ParamConst.ding.appsecret);
        
        Map<String, Object> req = new HashMap<>();
        req.put("appkey", appKey);
        req.put("appsecret", appsecret);
        String respStr = HttpUtil.creart().doGet().send(DingRobot.ding  + "/gettoken", req);
        JsonTools resp = JsonTools.parseObject(respStr);
        return resp.getString("access_token");
    }



    private class TokenCache {
        
        private String token;
        private long time = 0L;
        
        public String getToken() {
            long now = System.currentTimeMillis();
            if( now - time > 6600000L || StringUtils.isBlank(token)){//重新获取token
                token = dingtalkGetToken();
                time = now;
            }
            return token;
        }
    }
    
    
    

    /**
     * 根据用户id 发送消息
     * */
    @Override
    public void sendMsgByUserId (String userIds, String title, String body, String comment, String... strs){

        String agentId = paramService.getValue(ParamConst.ding.agentId);
        if ( StringUtils.isBlank(agentId) ) {
            return;
        }

        DingRobotMessage message = new DingRobotMessage();
        message.setTitle(StringUtils.defaultIfBlank(title, "新消息") + " [" + DateUtil.ll.getDate() + "]");
        message.add(body);

        if( StringUtils.isNotBlank(comment) ){

            String imghost = appConfig.getZentaoWebHost() + "/file-read-";

            StringBuilder sbr = new StringBuilder(comment);
            Matcher img = imgPattern.matcher(comment);
            while( img.find() ){
                String imgname = img.group(2);
                String imageText = DingRobotMessage.getImageText(imghost + imgname);
                int left = img.start(1);
                int right = img.end(1);
                sbr.replace(left, right, imageText);
            }
            message.add(DingRobot.NEW_LINE + sbr);
        }
        if( strs != null ){
            Arrays.stream(strs).forEach(str -> message.add(DingRobot.NEW_LINE + str));
        }

        try {
            Map<String, Object> req = new HashMap<>();
            req.put("access_token", tokenCache.getToken());    //tokenCache.getToken()
            req.put("lang", "zh_CN");
            req.put("agent_id", agentId);
            req.put("msg", message.toMarkdownJson());
            req.put("userid_list", userIds);
            HttpUtil.creart().doPost().send(DingRobot.ding + "/topapi/message/corpconversation/asyncsend_v2", req);
        } catch ( Exception ex ) {
            throw BusinessException.builder(ex);
        }
    }





}
