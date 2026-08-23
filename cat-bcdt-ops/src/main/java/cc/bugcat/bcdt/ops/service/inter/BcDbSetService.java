package cc.bugcat.bcdt.ops.service.inter;

import cc.bugcat.acore.mybatis.plus.IBaseService;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author auto
 * 2026-07-31 14:31:59
 * */
public interface BcDbSetService extends IBaseService<BcDbSet> {
    

    /**
	 * 如果有批操作，请继承 cc.bugcat.common.framework.service.BaseBatchService
	 * */


	BcDbSet getByCode(String databaseCode);

	Map<Long, BcDbSet> getDbSetMap();


	BcDbSet selectByName(String name);
	List<String> selectByBean(String name);

}
