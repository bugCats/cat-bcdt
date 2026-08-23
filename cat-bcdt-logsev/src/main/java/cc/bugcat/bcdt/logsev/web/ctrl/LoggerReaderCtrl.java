
package cc.bugcat.bcdt.logsev.web.ctrl;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.bcdt.logsev.config.AppWorkspace;
import cc.bugcat.bcdt.logsev.config.LoggerConfig;
import cc.bugcat.bcdt.logsev.modules.account.AccountSession;
import cc.bugcat.bcdt.logsev.modules.account.AccountSessionFactory;
import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSessionFactory;
import cc.bugcat.bcdt.logsev.modules.reader.handle.ReadStopHandle;
import cc.bugcat.bcdt.logsev.modules.reader.handle.vi.ReaderInfoVi;
import cc.bugcat.bcdt.logsev.utils.Tools;
import cc.bugcat.bcdt.logsev.web.vo.AppsVo;
import cc.bugcat.bcdt.logsev.web.vo.LogFileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author bugcat-cc
 * */
@RestController
public class LoggerReaderCtrl {
    
    
    @Autowired
    private LoggerConfig loggerConfig;
    
    @Autowired
    private AccountSessionFactory sessionFactory;
    
    @Autowired
    private ReadStopHandle readStopHandle;

    
    /**
     * 获取所有应用信息
     * */
    @PostMapping(value={"/logger/apps"})
    public ResultEntity<List<AppsVo>> queryApps(@RequestHeader("token") String user) {
        this.sessionFactory.getSession(user);
        
        List<AppsVo> list = new ArrayList<>();
        List<AppWorkspace> workspace = this.loggerConfig.getWorkspace();
        for (AppWorkspace ws : workspace) {
            String label = ws.getLabel();
            List<String> apps = ws.getApps();
            for (String app : apps) {
                AppsVo vo = new AppsVo();
                vo.setAppId(label + "@#" + app);
                vo.setLabel(label);
                vo.setAppName(app);
                list.add(vo);
            }
        }
        return ResultEntity.ok(list);
    }

    
    /**
     * 获取指定应用的日志文件列表
     * */
    @PostMapping(value={"/logger/fileList"})
    public ResultEntity<List<LogFileInfo>> fileList(@RequestHeader("token") String user, @Validated @RequestBody ReaderInfoVi reqVi) {
        
        AccountSession session = this.sessionFactory.getSession(user);
        
        String appId = reqVi.getAppId();
        String path = this.loggerConfig.getPath(appId);
        String logsName = reqVi.getLogName() + "/logs/";
        int logsNameLength = logsName.length();
        
        ReaderFileSessionFactory factory = session.getReaderFactory();
        List<File> logFiles = factory.getLogFile(path);

        List<LogFileInfo> list = new LinkedList<>();
        Tools.findFiles(logFiles, list);
        list.sort((o1, o2) -> o1.getLogName().compareToIgnoreCase(o2.getLogName()));

        String now = Tools.dateToStr(new Date());
        for (LogFileInfo bean : list) {
            bean.setDate(now);
            String logName = bean.getLogName();
            int idx = logName.indexOf(logsName);
            if (idx > -1) {
                bean.setLogName(logName.substring(idx + logsNameLength));
                continue;
            }
            if (logName.endsWith("/nohup.out")) {
                bean.setLogName("nohup.out");
            }
        }
        return ResultEntity.ok(list);
    }
    
    

    /**
     * 关闭文件
     * */
    @PostMapping(value={"/logger/closeLogReader"})
    public ResultEntity<Void> closeLogReader(@RequestHeader("token") String userId, @Validated @RequestBody ReaderInfoVi reqVi) {
        try {
            AccountSession session = this.sessionFactory.getSession(userId);
            ReaderFileSessionFactory factory = session.getReaderFactory();
            factory.destroy(reqVi);
        } catch (Exception ex) {
            // noop
        }
        return ResultEntity.ok();
    }

    
    /**
     * 关闭搜索
     * */
    @PostMapping(value={"/logger/search/stop"})
    public ResultEntity<Void> searchStop(@RequestHeader(value="token") String userId, @Validated @RequestBody ReaderInfoVi reqVi) {
        AccountSession session = this.sessionFactory.getSession(userId);
        this.readStopHandle.postHandle(session, reqVi);
        return ResultEntity.ok();
    }
}

