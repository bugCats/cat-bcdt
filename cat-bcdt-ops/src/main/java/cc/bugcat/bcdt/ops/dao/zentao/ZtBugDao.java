package cc.bugcat.bcdt.ops.dao.zentao;

import cc.bugcat.acore.mybatis.plus.IBaseDao;
import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.model.zentao.ZtBug;
import org.springframework.stereotype.Repository;


@Repository
public interface ZtBugDao extends IBaseDao<ZtBug> {
    
    default String findZtBugAssignedToById(Long id) {
        return ObjectUtil.getFirst(selectString(MyWrapper.of(ZtBug.class).select(ZtBug::getAssignedTo).eq(ZtBug::getId, id)));
    }
    
}
