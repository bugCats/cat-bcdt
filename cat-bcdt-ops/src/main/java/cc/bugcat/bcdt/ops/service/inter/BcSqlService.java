package cc.bugcat.bcdt.ops.service.inter;

import cc.bugcat.acore.mybatis.plus.IBaseService;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;
import cc.bugcat.bcdt.ops.model.zentao.BcSql;

/**
 *
 * @author auto
 * 2022-06-06 14:08:25
 * */
public interface BcSqlService extends IBaseService<BcSql> {

    
    String executeSql(BcSql bcSql, BcDbSet dbSet);
    
}
