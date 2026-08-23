package cc.bugcat.bcdt.ops.openapi.dbset.name;

import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.bcdt.ops.model.ext.BcDbSetExt;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;
import cc.bugcat.bcdt.ops.openapi.dbset.name.vi.DbsetNameVi;
import cc.bugcat.bcdt.ops.openapi.dbset.name.vo.DbsetNameVo;
import cc.bugcat.bcdt.ops.service.inter.BcDbSetService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class DbsetNameService extends AbstractOpenApiService<DbsetNameVi, List<DbsetNameVo>> {

	
	@Autowired
	private BcDbSetService dbSetService;

	
	@Override
	@Transactional(readOnly = true)
	public List<DbsetNameVo> execute (DbsetNameVi req, RequestContext context) {
		
		MyWrapper<BcDbSet> wrapper = MyWrapper.of(BcDbSet.class)
				.select(BcDbSet::getId, BcDbSet::getCode, BcDbSetExt::getDesc)
				.eq(BcDbSet::getActive, "1")
				.orderByAsc(BcDbSet::getCode);
		
		if ( req.getCryptoType() != null ) {
			if ( StringUtils.isBlank(req.getCryptoType()) ) {
				wrapper.ne(true, BcDbSet::getCryptoType, "");
			} else {
				wrapper.eq(BcDbSet::getCryptoType, req.getCryptoType());
			}
		}
		
		List<BcDbSet> list = dbSetService.list(wrapper);
		
		return BeanCopyUtil.copys(list, DbsetNameVo::new);
	}
}
