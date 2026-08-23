package cc.bugcat.bcdt.ops.openapi.jobs;


import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.model.zentao.BcSqlTask;
import cc.bugcat.bcdt.ops.model.zentao.BcTalk;
import cc.bugcat.bcdt.ops.service.inter.BcSqlTaskService;
import cc.bugcat.bcdt.ops.service.mysql.handler.SessionManager;
import cc.bugcat.bcdt.ops.web.Payloads;
import cc.bugcat.bcdt.ops.web.WebAction;
import cc.bugcat.bcdt.ops.web.WebTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * 定时任务
 *
 *
 * @author bugcat-cc
 */
@Component
public class TimerJob {
    
    private static final Logger logger = LoggerFactory.getLogger(TimerJob.class);
    
    
    @Autowired
    private BcSqlTaskService sqlTaskService;
    
    
    
    /**
     * 定时执行sql计划任务
     * */
    @Scheduled(cron = "0 0 0-6 * * ?")    // 00:00-05:00每小时
    @Scheduled(cron = "0 0 18-23 * * ?")  // 20:00-23:59每小时
    public void dbTask(){
        logger.info("数据库计划任务开始执行...");
        
        Date now = new Date();
        List<BcSqlTask> tasks = sqlTaskService.list(MyWrapper.of(BcSqlTask.class)
                .eq(BcSqlTask::getStatus, "10")
                .le(BcSqlTask::getTaskDate, now));

        logger.info("计划执行任务数量：{}", tasks.size());
        
        if ( ObjectUtil.isNotEmpty(tasks) ) {
            sqlTaskService.executeTask(tasks);
        }

        logger.info("数据库计划任务执行结束");
    }



    /**
     * 检查数据库代理链接
     * */
    @Scheduled(cron = "0 */3 * * * ?")
    public void dbProxyPatrol() {
        SessionManager manager = SessionManager.getManager();
        manager.check();
    }
    
    
    
    /**
     * 打卡提醒
     * */
    @Scheduled(cron = "0 58 08 * * ?")
    @Scheduled(cron = "0 32 18 * * ?")
    public void workFinish(){
        JsonTools payload = Payloads.notifyBuilder()
                .title("【考勤提醒】")
                .content("工作再忙，也要记得打卡！")
                .msgType(BcTalk.TextType.web_sms.code)
                .build();
        WebTopic.builder()
                .webSessions("---") //后台组
                .action(WebAction.web_alert)
                .payload(payload)
                .notify(true)
                .doSend();
    }
    
    
}
