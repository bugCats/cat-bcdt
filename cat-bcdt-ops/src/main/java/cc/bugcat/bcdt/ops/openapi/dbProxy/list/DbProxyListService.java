package cc.bugcat.bcdt.ops.openapi.dbProxy.list;

import cc.bugcat.acore.framework.entity.PageInfo;
import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.mybatis.plus.Pages;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.model.zentao.BcDbProxy;
import cc.bugcat.bcdt.ops.openapi.dbProxy.list.vi.DbProxyListVi;
import cc.bugcat.bcdt.ops.openapi.dbProxy.list.vo.DbProxyListVo;
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
public class DbProxyListService extends AbstractOpenApiService<DbProxyListVi, PageInfo<DbProxyListVo>> {

	@Autowired
	private BcDbProxyService dbProxyService;

	
	@Override
	@Transactional(readOnly = true)
	public PageInfo<DbProxyListVo> execute (DbProxyListVi req, RequestContext context) {
		Pages<BcDbProxy> page = dbProxyService.page(Pages.of(req), MyWrapper.of(BcDbProxy.class).orderByAsc(BcDbProxy::getId));
		return page.toPageInfo(DbProxyListVo::new, (src, dist) -> {
			if ( "1".equals(src.getEnable()) ) {
				dist.setEnableName("已启用");
			} else {
				dist.setEnableName("已禁用");
			}
		});
	}
}
