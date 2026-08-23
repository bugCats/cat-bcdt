package cc.bugcat.bcdt.ops.dao.zentao;

import cc.bugcat.acore.mybatis.annotation.Encrys;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import cc.bugcat.acore.mybatis.plus.IBaseDao;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;

import java.util.List;

/**
 * bc_db_set:数据库配置
 *
 * @author auto
 * 2026-07-31 14:31:59
 *
 */
@Repository
public interface BcDbSetDao extends IBaseDao<BcDbSet> {

    /**
     * 此处为代码自动生成
     * 方法上一定要加上注释说明！！！
     *
     */


    BcDbSet selectByName(@Param("username") @Encrys String name);

    @Encrys
    List<String> selectByBean(BcDbSet query);

}
