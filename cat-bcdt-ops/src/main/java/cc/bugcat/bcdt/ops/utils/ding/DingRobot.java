package cc.bugcat.bcdt.ops.utils.ding;

import cc.bugcat.acore.framework.utils.WorkPool;
import cc.bugcat.acore.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送钉钉群机器人消息
 */
public class DingRobot {

    public static final String NEW_LINE = " \n ";
    
    public static final String ding = "https://oapi.dingtalk.com";
    
    private DingRobot(){

    }

    private String token;
    private String title;
    private String body;
    private boolean atAll = false;
    private List<String> contents = new ArrayList<>();
    private List<String> atPhones = new ArrayList<>();

    public static DingRobot build(){
        return new DingRobot();
    }

    /**
     * 通知的群
     * */
    public DingRobot token(String token) {
        this.token = token;
        return this;
    }
    /**
     * 标题
     * */
    public DingRobot title(String title) {
        this.title = title;
        return this;
    }
    
    /**
     * 内容
     * */
    public DingRobot body(String body) {
        this.body = body;
        return this;
    }

    /**
     * 换行的描述
     * */
    public DingRobot content(List<String> contents) {
        this.contents = contents;
        return this;
    }

    /**
     * 换行的描述
     * */
    public DingRobot addContent(String content) {
        if( StringUtils.isNotBlank(content) ){
            this.contents.add(content);
        }
        return this;
    }
    
    /**
     * at all 
     * */
    public DingRobot atAll(boolean atAll) {
        this.atAll = atAll;
        return this;
    }
    
    /**
     * 需要at的手机号
     * */
    public DingRobot addAtPhone(String phone) {
        if( StringUtils.isNotBlank(phone) ){
            this.atPhones.add(phone);
        }
        return this;
    }
    
    
    
    public void send() {
        if( StringUtils.isBlank(token) ){
            return;
        }
        WorkPool.createThread(() -> {
            String webhook = ding + "/robot/send?access_token=" + token;
            DingRobotMessage message = new DingRobotMessage();
            message.setTitle(StringUtils.defaultIfBlank(title, "消息") + System.currentTimeMillis());
            message.setAtAll(atAll);
            message.setAtPhone(atPhones);
            message.add(body);
            contents.forEach(str -> message.add(NEW_LINE + str));
            String markdown = message.toMarkdownJson();
            HttpUtil.creart().jsonPost().send(webhook, markdown);
        });
    }
    
    
    
    public void send(List<String[]> bottonList){
        if( StringUtils.isBlank(token) ){
            return;
        }
        WorkPool.createThread(() -> {
            String webhook = ding + "/robot/send?access_token=" + token;
            DingRobotMessage message = new DingRobotMessage();
            message.setTitle(StringUtils.defaultIfBlank(title, "消息") + System.currentTimeMillis());
            message.setAtAll(atAll);
            message.setAtPhone(atPhones);
            message.add(body);
            contents.forEach(str -> message.add(NEW_LINE + str));
            String actionCard = message.toActionCard(bottonList);
            HttpUtil.creart().jsonPost().send(webhook, actionCard);
        });
    }
    
}
