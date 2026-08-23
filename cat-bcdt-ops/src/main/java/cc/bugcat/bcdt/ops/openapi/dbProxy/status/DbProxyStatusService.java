package cc.bugcat.bcdt.ops.openapi.dbProxy.status;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.model.zentao.BcDbProxy;
import cc.bugcat.bcdt.ops.openapi.dbProxy.status.vi.DbProxyStatusVi;
import cc.bugcat.bcdt.ops.openapi.listeners.events.DbProxyStatusEvent;
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
public class DbProxyStatusService extends AbstractOpenApiService<DbProxyStatusVi, Void> {

	@Autowired
	private BcDbProxyService dbProxyService;

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Void execute (DbProxyStatusVi req, RequestContext context) {

		BcDbProxy dbProxy = dbProxyService.getById(req.getId());
		if (dbProxy == null) {
			throw BusinessException.builder(Errors.ERROR_PARAM, "错误的数据id");
		}

		boolean enable = "1".equals(req.getEnable());
        if ( enable ) {
            dbProxyService.update(MyWrapper.of(BcDbProxy.class).set(BcDbProxy::getEnable, "1").eq(BcDbProxy::getId, req.getId()));
        } else {
			dbProxyService.update(MyWrapper.of(BcDbProxy.class).set(BcDbProxy::getEnable, "0").eq(BcDbProxy::getId, req.getId()));
		}
		DbProxyStatusEvent.with(this).id(req.getId()).publishEvent();
		return null;
	}
}
