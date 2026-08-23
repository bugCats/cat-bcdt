package cc.bugcat.bcdt.ops.service.inter;


import cc.bugcat.acore.framework.entity.PageInfo;
import cc.bugcat.bcdt.ops.model.TaskWork;
import cc.bugcat.bcdt.ops.openapi.zentao.all.vi.ZenTaoTaskVi;

import java.util.Collection;
import java.util.List;

public interface ZenTaoService {

    String findAccountByObjectId(String objectType, Long objectId);

    
    List<TaskWork> findByObjectId(Long objectId);

    
    List<TaskWork> findProjectByTypeAndIds(Integer objectType, Collection<Long> objectIdIds);


    //查询所有任务
    PageInfo<TaskWork> findAllWork(ZenTaoTaskVi query);

    //查询7天内指派给我的、创建人是我
    List<TaskWork> findMyItem(TaskWork query);
}
