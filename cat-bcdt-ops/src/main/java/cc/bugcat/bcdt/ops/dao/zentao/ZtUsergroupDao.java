package cc.bugcat.bcdt.ops.dao.zentao;

import cc.bugcat.acore.mybatis.plus.IBaseDao;
import cc.bugcat.bcdt.ops.model.zentao.ZtUsergroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * zt_usergroup:get it
 * @author auto
 * 2022-06-02 18:09:01
 * */
@Repository
public interface ZtUsergroupDao extends IBaseDao<ZtUsergroup> {
    
    
    /**
     * 获取禅道账号自定义的权限编号，注意：自定义权限编号，约定其描述字段，以#开头
     * */
    List<String> findOperByAccount(@Param("account") String account);
    
}
