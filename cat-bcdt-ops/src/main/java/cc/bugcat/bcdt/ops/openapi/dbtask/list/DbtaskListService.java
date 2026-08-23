package cc.bugcat.bcdt.ops.openapi.dbtask.list;

import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;
import cc.bugcat.bcdt.ops.model.zentao.BcSql;
import cc.bugcat.bcdt.ops.model.zentao.BcSqlTask;
import cc.bugcat.bcdt.ops.openapi.dbtask.list.vi.DbtaskListVi;
import cc.bugcat.bcdt.ops.openapi.dbtask.list.vo.DbtaskListVo;
import cc.bugcat.bcdt.ops.service.inter.BcDbSetService;
import cc.bugcat.bcdt.ops.service.inter.BcSqlTaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class DbtaskListService extends AbstractOpenApiService<DbtaskListVi, List<DbtaskListVo>> {

	
	@Autowired
	private BcSqlTaskService sqlTaskService;
	
	@Autowired
	private BcDbSetService dbSetService;
	
	
	@Override
	@Transactional(readOnly = true)
	public List<DbtaskListVo> execute (DbtaskListVi reqVi, RequestContext context) {
		
		
		BcSql query = new BcSql();
		Date startDate = DateUtil.rd.startDateTime(reqVi.getStartDate());
		Date endDate = DateUtil.rd.endDateTime(reqVi.getEndDate());
		query.setStartDate(startDate);
		query.setEndDate(endDate);
		query.setDbId(reqVi.getDbId());
		query.setStatus(reqVi.getStatus());
		
		List<BcSqlTask> list = sqlTaskService.list(MyWrapper.of(BcSqlTask.class)
				.in(BcSqlTask::getId, query.getIds())
				.eq(query::getAccount)
				.eq(query::getDbId)
				.eq(query::getStatus)
				.ge(BcSqlTask::getCreateDate, query.getStartDate())
				.le(BcSqlTask::getCreateDate, query.getEndDate())
				.orderByAsc(BcSqlTask::getId)
		);

		Map<Long, BcDbSet> dbSetMap = dbSetService.getDbSetMap();

		List<DbtaskListVo> result = BeanCopyUtil.copys(list, DbtaskListVo::new, (src, dist) -> {
			BcDbSet dbSet = dbSetMap.get(dist.getDbId());
			if (dbSet != null) {
				dist.setDbCode(dbSet.getCode());
				dist.setDbDesc(StringUtils.defaultIfBlank(dbSet.getDesc(), dbSet.getCode()));
			}
			dist.setResult(StringUtils.defaultIfBlank(src.getResult(), "未执行"));
		});
		
		return result;
	}
}
