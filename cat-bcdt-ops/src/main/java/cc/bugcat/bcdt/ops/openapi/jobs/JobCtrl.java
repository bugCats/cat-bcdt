package cc.bugcat.bcdt.ops.openapi.jobs;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.framework.utils.WorkPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class JobCtrl {
    
    @Autowired
    private TimerJob timerJob;
    
    
    @ACS(inner = true)
    @GetMapping("/jobs/test/dbTask")
    public ResultEntity<Void> dbTask(){
        WorkPool.createThread(() -> timerJob.dbTask());
        return ResultEntity.ok();
    }
    
}
