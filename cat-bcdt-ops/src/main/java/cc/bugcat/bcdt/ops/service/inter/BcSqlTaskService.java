package cc.bugcat.bcdt.ops.service.inter;

import cc.bugcat.acore.mybatis.plus.IBaseService;
import cc.bugcat.bcdt.ops.model.zentao.BcSqlTask;

import java.util.List;

/**
 * 
 * @author auto
 * 2025-09-16 15:06:13
 * */
public interface BcSqlTaskService extends IBaseService<BcSqlTask> {

	
	/**
	 * 执行定时任务
	 * */
	void executeTask(List<BcSqlTask> tasks);
	
	
	void updateTaskStatus(BcSqlTask task);
}
