package cc.bugcat.bcdt.ops.dao.zentao;

import cc.bugcat.acore.mybatis.plus.IBaseDao;
import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.model.zentao.ZtStory;
import org.springframework.stereotype.Repository;


@Repository
public interface ZtStoryDao extends IBaseDao<ZtStory> {
    
    default String findZtStoryAssignedToById(Long id) {
        return ObjectUtil.getFirst(selectString(MyWrapper.of(ZtStory.class).select(ZtStory::getAssignedTo).eq(ZtStory::getId, id)));
    }
}
