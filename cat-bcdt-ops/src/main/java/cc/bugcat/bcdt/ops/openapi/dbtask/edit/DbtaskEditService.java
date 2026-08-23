package cc.bugcat.bcdt.ops.openapi.dbtask.edit;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.model.zentao.BcSqlTask;
import cc.bugcat.bcdt.ops.openapi.dbtask.edit.vi.DbtaskEditVi;
import cc.bugcat.bcdt.ops.service.inter.BcSqlTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class DbtaskEditService extends AbstractOpenApiService<DbtaskEditVi, Void> {
	
	@Autowired
	private BcSqlTaskService sqlTaskService;

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Void execute (DbtaskEditVi reqVi, RequestContext context) {
		
		BcSqlTask sqlTask = sqlTaskService.getById(reqVi.getId());

		if( "2".equals(reqVi.getType()) ){ //删除，只有10才能删除
			if( !"10".equals(sqlTask.getStatus()) ){
				throw BusinessException.builder("500", Errors.SYS_ERR, "只有未执行的数据才能删除！");
			}
			sqlTaskService.removeById(reqVi.getId());
		} 
		
		return null;
	}
}
