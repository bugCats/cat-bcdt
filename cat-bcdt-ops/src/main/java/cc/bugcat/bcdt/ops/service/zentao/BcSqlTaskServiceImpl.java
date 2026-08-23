package cc.bugcat.bcdt.ops.service.zentao;

import cc.bugcat.acore.mybatis.plus.BaseService;
import cc.bugcat.acore.framework.utils.SpringContextUtil;
import cc.bugcat.bcdt.ops.dao.zentao.BcSqlTaskDao;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;
import cc.bugcat.bcdt.ops.model.zentao.BcSql;
import cc.bugcat.bcdt.ops.model.zentao.BcSqlTask;
import cc.bugcat.bcdt.ops.service.inter.BcDbSetService;
import cc.bugcat.bcdt.ops.service.inter.BcSqlService;
import cc.bugcat.bcdt.ops.service.inter.BcSqlTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * BcSqlTaskDao 已经被注入，在该类中，直接使用 dao.XX() 
 * @author auto
 * 2025-09-16 15:06:13
 * */
@Service
public class BcSqlTaskServiceImpl extends BaseService<BcSqlTaskDao, BcSqlTask> implements BcSqlTaskService {
    
    @Autowired
    private BcSqlService bcSqlService;
    
    @Autowired
    private BcDbSetService dbSetService;
    
    
    @Override
    public void executeTask(List<BcSqlTask> tasks) {
        
        Map<Long, List<BcSqlTask>> taskMap = tasks.stream().collect(Collectors.groupingBy(BcSqlTask::getDbId));
        
        BcSqlTaskService self = SpringContextUtil.getBean(BcSqlTaskService.class);
        
        for ( Map.Entry<Long, List<BcSqlTask>> entry : taskMap.entrySet() ) {
            Long dbId = entry.getKey();
            BcDbSet dbSet = dbSetService.getById(dbId);
            List<BcSqlTask> list = entry.getValue();
            for ( BcSqlTask task : list ) {
                long startTime = System.currentTimeMillis();

                BcSql bcSql = new BcSql();
                bcSql.setSql(task.getSql());
                String errMsg = bcSqlService.executeSql(bcSql, dbSet);
                
                long endTime = System.currentTimeMillis();
                long costTime = endTime - startTime;
                
                BcSqlTask uper = new BcSqlTask();
                uper.setId(task.getId());
                uper.setExecuteDate(new Date());
                
                if ( StringUtils.isNotBlank(errMsg) ) { // 执行失败
                    uper.setStatus("90");
                    uper.setResult(String.format("执行失败！耗时：%d ms，错误原因：%s", costTime, errMsg));
                } else {
                    uper.setStatus("20");
                    uper.setResult(String.format("执行成功！耗时：%d ms", costTime));
                }
                self.updateTaskStatus(uper);
            }
        }
    }
    
    
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void updateTaskStatus(BcSqlTask task) {
        super.updateById(task);
    }
}