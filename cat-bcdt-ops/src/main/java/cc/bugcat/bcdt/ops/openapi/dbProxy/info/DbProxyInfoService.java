package cc.bugcat.bcdt.ops.openapi.dbProxy.info;

import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.bcdt.ops.model.zentao.BcDbProxy;
import cc.bugcat.bcdt.ops.openapi.dbProxy.info.vi.DbProxyInfoVi;
import cc.bugcat.bcdt.ops.openapi.dbProxy.info.vo.DbProxyInfoVo;
import cc.bugcat.bcdt.ops.service.inter.BcDbProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class DbProxyInfoService extends AbstractOpenApiService<DbProxyInfoVi, DbProxyInfoVo> {

	
	@Autowired
	private BcDbProxyService dbProxyService;

	
	@Override
	@Transactional(readOnly = true)
	public DbProxyInfoVo execute (DbProxyInfoVi req, RequestContext context) {
		BcDbProxy dbProxy = dbProxyService.getById(req.getId());
		return BeanCopyUtil.copy(dbProxy, DbProxyInfoVo::new);
	}
}
