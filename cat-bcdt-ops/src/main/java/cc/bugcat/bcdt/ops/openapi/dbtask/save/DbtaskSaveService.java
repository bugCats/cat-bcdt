package cc.bugcat.bcdt.ops.openapi.dbtask.save;

import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.ops.model.zentao.BcSqlTask;
import cc.bugcat.bcdt.ops.openapi.dbtask.save.vi.DbtaskSaveVi;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.service.inter.BcSqlTaskService;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class DbtaskSaveService extends AbstractOpenApiService<DbtaskSaveVi, Void> {
	
	
	@Autowired
	private BcSqlTaskService sqlTaskService;

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Void execute (DbtaskSaveVi reqVi, RequestContext context) {
		
		UserInfo user = UserUtil.getUser(context);
		
		Date now = new Date();

		String tasktime = reqVi.getTaskDate();
		Date taskdate = null;
		
		if ( tasktime.startsWith("@") ) {
			String next = DateUtil.rs.plusDay(now, 1);
			tasktime = tasktime.substring(1);
			taskdate = DateUtil.ll.parseDate(next + " " + tasktime + ":00");
		} else {
			String next = DateUtil.ss.format(now);
			taskdate = DateUtil.ll.parseDate(next + " " + tasktime + ":00");
		}
		
		BcSqlTask task = new BcSqlTask();
		task.setAccount(user.getAccount());
		task.setRealname(user.getRealname());
		task.setCreateDate(now);
		task.setSql(reqVi.getSql());
		task.setDesc(reqVi.getDesc());
		task.setDbId(reqVi.getDbId());
		task.setStatus("10");
		task.setTaskDate(taskdate);

		sqlTaskService.save(task);
		
		return null;
	}
}
