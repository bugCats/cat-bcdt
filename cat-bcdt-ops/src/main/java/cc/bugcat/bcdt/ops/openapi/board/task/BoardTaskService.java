package cc.bugcat.bcdt.ops.openapi.board.task;

import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.dao.zentao.BcNoteDao;
import cc.bugcat.bcdt.ops.model.TaskWork;
import cc.bugcat.bcdt.ops.model.zentao.BcNote;
import cc.bugcat.bcdt.ops.openapi.board.task.vi.BoardTaskVi;
import cc.bugcat.bcdt.ops.openapi.board.task.vo.BoardTask;
import cc.bugcat.bcdt.ops.openapi.board.task.vo.BoardTaskVo;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.service.inter.ZenTaoService;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class BoardTaskService extends AbstractOpenApiService<BoardTaskVi, BoardTaskVo> {
    
    @Autowired
    private BcNoteDao bcNoteDao;
    
    @Autowired
    private ZenTaoService zenTaoService;
    
    @Override
    @Transactional(readOnly = true)
    public BoardTaskVo execute(BoardTaskVi reqVi, RequestContext context) {
        
        UserInfo user = UserUtil.getUser(context);
        
        if ( "other".equals(reqVi.getDateType()) && StringUtils.isAnyBlank(reqVi.getStart(), reqVi.getEnd()) ) {
            reqVi.setDateType("week");
        }
        
        final String dateType = reqVi.getDateType(); // week，month，year，other
        final boolean isteam = Opers.hasOper(user, Opers.teamNote) && "5".equals(reqVi.getDataRange());
        
        String start = null;
        String end = null;
        
        if ( "other".equals(dateType) ) {
            
            start = reqVi.getStart();
            end = reqVi.getEnd();
            
        } else {
            
            // 创建格式化器（指定格式为 yyyy-MM-dd）
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            // 获取当前日期
            LocalDate today = LocalDate.now();
            end = today.format(formatter);

            switch ( dateType ) {
                case "week" -> start = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format(formatter);
                case "month" -> start = today.with(TemporalAdjusters.firstDayOfMonth()).format(formatter);
                case "year" -> start = today.with(TemporalAdjusters.firstDayOfYear()).format(formatter);
                case null, default -> {
                    return null;
                }
            }
        }
        
        List<Map<String, Object>> list = bcNoteDao.selectMaps(MyWrapper.of(BcNote.class)
				.select("CONCAT(a.projectId, '_', a.projectType) AS 'project'", "SUM(a.costTime) AS 'costTime'", "GROUP_CONCAT( DISTINCT a.realname) AS 'joins'")
				.from("a")
                .ne(BcNote::getProjectId, 0)
				.eq(BcNote::getProjectType, reqVi.getProjectType())
				.eq(!isteam, BcNote::getAccount, user.getAccount())
				.between(BcNote::getSubmitDate, start, end)
				.groupBy("project")
				.orderByDesc("costTime")
				.limit(20));
        
        Map<String, Set<Long>> projectMap = new HashMap<>();
        for ( Map<String, Object> map : list ) {
            String[] values = ObjectUtil.getString(map, "project").split("_");
            Long projectId = Long.parseLong(values[0]);
            String projectType = values[1];
            map.put("projectId", projectId);
            map.put("projectType", projectType);
            
            Set<Long> ids = projectMap.get(projectType);
            if ( ids == null ) {
                ids = new HashSet<>();
                projectMap.put(projectType, ids);
            }
            ids.add(projectId);
        }
        
        
        Map<String, String> noteMap = new HashMap<>();
        for ( Map.Entry<String, Set<Long>> entry : projectMap.entrySet() ) {
            String type = entry.getKey();
            Set<Long> ids = entry.getValue();
            List<TaskWork> items = zenTaoService.findProjectByTypeAndIds(Integer.parseInt(type), ids);
            for ( TaskWork item : items ) {
                noteMap.put(type + ":" + item.getObjectId(), item.getObjectName());
            }
        }
        
        List<BoardTask> tasks = new ArrayList<>(list.size());
        for ( Map<String, Object> map : list ) {
            BoardTask task = new BoardTask();
            tasks.add(task);
            Long projectId = ObjectUtil.getLong(map, "projectId");
            String projectType = ObjectUtil.getString(map, "projectType");
            String projectName = noteMap.get(projectType + ":" + projectId);
            String joins = ObjectUtil.getString(map, "joins");
            int userCount = StringUtils.split(joins, ",").length;
            
            task.setProjectId(projectId);
            task.setProjectType(projectType);
            task.setProjectName(projectName);
            task.setCostTime(ObjectUtil.getString(map, "costTime"));
            task.setJoins(String.format("%s 共%s人参与", joins, userCount));
        }
        
        BoardTaskVo resp = new BoardTaskVo();
        resp.setStart(start);
        resp.setEnd(end);
        resp.setList(tasks);
        
        return resp;
    }
}
