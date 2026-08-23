package cc.bugcat.bcdt.ops.openapi.zentao.list;

import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.ops.model.TaskWork;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.openapi.zentao.list.vi.ZenTaoListVi;
import cc.bugcat.bcdt.ops.openapi.zentao.list.vo.ZenTaoListVo;
import cc.bugcat.bcdt.ops.service.inter.ZenTaoService;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class ZenTaoListService extends AbstractOpenApiService<ZenTaoListVi, List<ZenTaoListVo>> {
    
    
    private final List<String> taskSql = Stream.of(
            List.of("openedBy", "openedDate"),
            List.of("assignedTo", "assignedDate"),
            List.of("finishedBy", "finishedDate"),
            List.of("canceledBy", "canceledDate"),
            List.of("closedBy", "closedDate"),
            List.of("lastEditedBy", "lastEditedDate")
    ).flatMap(Collection::stream).toList();
    
    private final List<String> bugSql = Stream.of(
            List.of("openedBy", "openedDate"),
            List.of("assignedTo", "assignedDate"),
            List.of("resolvedBy", "resolvedDate"),
            List.of("closedBy", "closedDate"),
            List.of("lastEditedBy", "lastEditedDate")
    ).flatMap(Collection::stream).toList();
    
    private final List<String> storySql = Stream.of(
            List.of("openedBy", "openedDate"),
            List.of("assignedTo", "assignedDate"),
            List.of("lastEditedBy", "lastEditedDate"),
            List.of("changedBy", "changedDate"),
            List.of("reviewedBy", "reviewedDate"),
            List.of("closedBy", "closedDate")
    ).flatMap(Collection::stream).toList();
    
    private final List<String> caseSql = Stream.of(
            List.of("openedBy", "openedDate"),
            List.of("reviewedBy", "reviewedDate"),
            List.of("lastEditedBy", "lastEditedDate")
    ).flatMap(Collection::stream).toList();
    
    
    @Autowired
    private ZenTaoService zenTaoService;
    
    

    @Override
    public List<ZenTaoListVo> execute(ZenTaoListVi reqVi, RequestContext context) {

        UserInfo user = UserUtil.getUser(context);

        Long objectId = null;
        try { objectId = Long.parseLong(reqVi.getKeyword()); } catch ( Exception ex ) { }

        List<TaskWork> list = null;

        if ( objectId != null ) {
            
            list = zenTaoService.findByObjectId(objectId);
            
        } else {
            
            Date now = new Date();
            Date start = DateUtil.rd.startDateTime(DateUtil.rd.plusDay(now, -7));
            Date end = DateUtil.rd.endDateTime(now);

            Map<String, List<String>> sqlMap = new HashMap<>();
            sqlMap.put("zt_task", taskSql);
            sqlMap.put("zt_bug", bugSql);
            sqlMap.put("zt_story", storySql);
            sqlMap.put("zt_case", caseSql);
            
            TaskWork query = new TaskWork();
            query.setAccount(user.getAccount());
            query.setObjectName(reqVi.getKeyword());
            query.setStartTime(start);
            query.setEndTime(end);
            query.setWhereSql(sqlMap);
            
            list = zenTaoService.findMyItem(query);
            
        }
        

        TaskWork def = new TaskWork();
        def.setObjectId(0L);
        def.setObjectName("日常工作");
        def.setObjectType(10);
        
        List<TaskWork> result = new ArrayList<>(list.size() + 1);
        result.add(def);
        result.addAll(list);
        return BeanCopyUtil.copys(result, ZenTaoListVo::new);
    }


}
