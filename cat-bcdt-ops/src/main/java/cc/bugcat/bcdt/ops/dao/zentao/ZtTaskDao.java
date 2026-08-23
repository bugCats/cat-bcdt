package cc.bugcat.bcdt.ops.dao.zentao;

import cc.bugcat.acore.mybatis.plus.IBaseDao;
import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.model.zentao.ZtTask;
import org.springframework.stereotype.Repository;

@Repository
public interface ZtTaskDao extends IBaseDao<ZtTask> {
    
    default String findZtTaskAssignedToById(Long id) {
        return ObjectUtil.getFirst(selectString(MyWrapper.of(ZtTask.class).select(ZtTask::getAssignedTo).eq(ZtTask::getId, id)));
    }
    
    
}
