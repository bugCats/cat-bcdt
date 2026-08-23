package cc.bugcat.bcdt.ops.dao.zentao;


import cc.bugcat.bcdt.ops.model.TaskWork;
import cc.bugcat.bcdt.ops.openapi.zentao.all.vi.ZenTaoTaskVi;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ZenTaoDao {
    
    /**
     * 根据数据id，查询代办、Bug、需求、任务、用例
     * */
    List<TaskWork> findByObjectId(@Param("objectId") Long objectId);

    
    List<TaskWork> findProjectByTypeAndIds(@Param("objectType") Integer objectType, @Param("objectIds") Collection<Long> objectIds);


    Long findAllWorkCount(ZenTaoTaskVi query);
    List<TaskWork> findAllWorkList(ZenTaoTaskVi query);


    /**
     * 查询指派人、被指派人是当前人
     * */
    List<TaskWork> findMyItem(TaskWork query);
    
}
