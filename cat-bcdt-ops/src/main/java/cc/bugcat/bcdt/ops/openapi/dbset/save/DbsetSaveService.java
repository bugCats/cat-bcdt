package cc.bugcat.bcdt.ops.openapi.dbset.save;

import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;
import cc.bugcat.bcdt.ops.service.inter.BcDbSetService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.bcdt.ops.openapi.dbset.save.vi.DbsetSaveVi;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class DbsetSaveService extends AbstractOpenApiService<DbsetSaveVi, Void> {

	@Autowired
	private BcDbSetService dbSetService;

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Void execute (DbsetSaveVi req, RequestContext context) {
		req.validate();
		BcDbSet dbSet = BeanCopyUtil.copy(req, BcDbSet::new);
		dbSet.setCryptoType(StringUtils.trimToEmpty(req.getCryptoType()));
		dbSet.setActive("1");
		if ( req.getId() != null ) {
			dbSetService.updateById(dbSet);
		} else {
			dbSetService.save(dbSet);
		}
		return null;
	}
}
