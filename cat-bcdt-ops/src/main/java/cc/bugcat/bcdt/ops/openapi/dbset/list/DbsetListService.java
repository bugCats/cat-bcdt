package cc.bugcat.bcdt.ops.openapi.dbset.list;

import cc.bugcat.acore.framework.entity.PageInfo;
import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.mybatis.plus.Pages;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;
import cc.bugcat.bcdt.ops.service.inter.BcDbSetService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.bcdt.ops.openapi.dbset.list.vi.DbsetListVi;
import cc.bugcat.bcdt.ops.openapi.dbset.list.vo.DbsetListVo;

import java.util.List;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class DbsetListService extends AbstractOpenApiService<DbsetListVi, PageInfo<DbsetListVo>> {

	@Autowired
	private BcDbSetService dbSetService;

	
	@Override
	@Transactional(readOnly = true)
	public PageInfo<DbsetListVo> execute (DbsetListVi req, RequestContext context) {

		List<String> pwd = dbSetService.selectByBean("root");
		System.out.println(pwd);
		BcDbSet dbSet = dbSetService.selectByName("root");
		System.out.println(dbSet.getPassword());


		Pages<BcDbSet> page = dbSetService.page(Pages.of(req), MyWrapper.of(BcDbSet.class).eq(BcDbSet::getActive, "1").orderByAsc(BcDbSet::getCode));
		return page.toPageInfo(DbsetListVo::new);
	}
}
