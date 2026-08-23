package cc.bugcat.bcdt.ops.openapi.dbset.info;

import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;
import cc.bugcat.bcdt.ops.service.inter.BcDbSetService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.bcdt.ops.openapi.dbset.info.vi.DbsetInfoVi;
import cc.bugcat.bcdt.ops.openapi.dbset.info.vo.DbsetInfoVo;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class DbsetInfoService extends AbstractOpenApiService<DbsetInfoVi, DbsetInfoVo> {

	@Autowired
	private BcDbSetService dbSetService;

	
	@Override
	@Transactional(readOnly = true)
	public DbsetInfoVo execute (DbsetInfoVi req, RequestContext context) {
		BcDbSet dbSet = dbSetService.getById(req.getId());
		return BeanCopyUtil.copy(dbSet, DbsetInfoVo::new);
	}
}
