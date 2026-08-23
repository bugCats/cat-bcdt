package cc.bugcat.bcdt.ops.openapi.dbProxy.save;

import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.bcdt.ops.model.zentao.BcDbProxy;
import cc.bugcat.bcdt.ops.openapi.dbProxy.save.vi.DbProxySaveVi;
import cc.bugcat.bcdt.ops.service.inter.BcDbProxyService;
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
public class DbProxySaveService extends AbstractOpenApiService<DbProxySaveVi, Void> {

	@Autowired
	private BcDbProxyService dbProxyService;

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Void execute (DbProxySaveVi req, RequestContext context) {
		req.validate();
		BcDbProxy dbProxy = BeanCopyUtil.copy(req, BcDbProxy::new);
		if ( req.getId() != null ) {
			dbProxyService.updateById(dbProxy);
		} else {
			dbProxyService.save(dbProxy);
		}
		return null;
	}
}
