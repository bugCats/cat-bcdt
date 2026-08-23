package cc.bugcat.bcdt.ops.service.zentao;

import cc.bugcat.acore.framework.entity.PageInfo;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.config.AppConfig;
import cc.bugcat.bcdt.ops.dao.zentao.ZenTaoDao;
import cc.bugcat.bcdt.ops.dao.zentao.ZtBugDao;
import cc.bugcat.bcdt.ops.dao.zentao.ZtStoryDao;
import cc.bugcat.bcdt.ops.dao.zentao.ZtTaskDao;
import cc.bugcat.bcdt.ops.model.TaskWork;
import cc.bugcat.bcdt.ops.openapi.zentao.all.vi.ZenTaoTaskVi;
import cc.bugcat.bcdt.ops.service.inter.ZenTaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ZenTaoServiceImpl implements ZenTaoService {
    
    @Autowired
    private AppConfig appConfig;
    
    @Autowired
    private ZenTaoDao zenTaoDao;
    
    @Autowired
    private ZtTaskDao ztTaskDao;
    
    @Autowired
    private ZtStoryDao ztStoryDao;
    
    @Autowired
    private ZtBugDao ztBugDao;
    
    
    
    @Override
    public String findAccountByObjectId(String objectType, Long objectId) {
        String account = null;
        if( "task".equals(objectType) ){
            account = ztTaskDao.findZtTaskAssignedToById(objectId);
        } else if( "story".equals(objectType) ){
            account = ztStoryDao.findZtStoryAssignedToById(objectId);
        } else if( "bug".equals(objectType) ){
            account = ztBugDao.findZtBugAssignedToById(objectId);
        }
        return account;
    }

    
    
    @Override
    public List<TaskWork> findByObjectId(Long objectId) {
        List<TaskWork> list = zenTaoDao.findByObjectId(objectId);
        return trimLink(list);
    }

    
    
    
    /**
     * 根据任务，查询任务
     * */
    @Override
    public List<TaskWork> findProjectByTypeAndIds(Integer objectType, Collection<Long> objectIdIds) {
        List<TaskWork> notes = zenTaoDao.findProjectByTypeAndIds(objectType, objectIdIds);
        return Optional.ofNullable(notes).orElseGet(Collections::emptyList);
    }


    /**
     * 查询所有任务
     */
    @Override
    public PageInfo<TaskWork> findAllWork(ZenTaoTaskVi query) {
        Long count = zenTaoDao.findAllWorkCount(query);
        if ( ObjectUtil.eq0(count) ) {
            return new PageInfo<>(query);
        }
        List<TaskWork> list = zenTaoDao.findAllWorkList(query);
        trimLink(list);

        PageInfo<TaskWork> pageInfo = new PageInfo<>(query, count);
        pageInfo.setList(list);
        return pageInfo;
    }


    @Override
    public List<TaskWork> findMyItem(TaskWork query) {
        List<TaskWork> list = zenTaoDao.findMyItem(query);
        return trimLink(list);
    }
    
    

    private List<TaskWork> trimLink(List<TaskWork> list) {
        String host = appConfig.getZentaoWebHost();
        Optional.ofNullable(list).orElseGet(Collections::emptyList).forEach(item -> {
            String type = 20 == item.getObjectType() ? "bug" : "task";
            item.setObjectLink(host + "/" + type + "-view-" + item.getObjectId() + ".html");
        });
        return list;
    }
}
