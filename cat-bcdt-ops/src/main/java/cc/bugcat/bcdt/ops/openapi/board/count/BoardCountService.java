package cc.bugcat.bcdt.ops.openapi.board.count;

import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.dao.zentao.BcNoteDao;
import cc.bugcat.bcdt.ops.model.zentao.BcNote;
import cc.bugcat.bcdt.ops.openapi.board.count.vi.BoardCountVi;
import cc.bugcat.bcdt.ops.openapi.board.count.vo.BoardCount;
import cc.bugcat.bcdt.ops.openapi.board.count.vo.BoardCountVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class BoardCountService extends AbstractOpenApiService<BoardCountVi, BoardCountVo> {
	
	@Autowired
	private BcNoteDao noteDao;

	@Override
	@Transactional(readOnly = true)
	public BoardCountVo execute (BoardCountVi reqVi, RequestContext context) {
		
		if ( "other".equals(reqVi.getDateType()) && StringUtils.isAnyBlank(reqVi.getStart(), reqVi.getEnd()) ) {
			reqVi.setDateType("week");
		}
		
		final String dateType = reqVi.getDateType(); // week，month，year，other
		
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
			
			if ( "week" .equals( dateType ) ) {
				start = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format(formatter);
			} else if ( "month" .equals( dateType ) ) {
				start = today.with(TemporalAdjusters.firstDayOfMonth()).format(formatter);
			} else if ( "year" .equals( dateType ) ) {
				start = today.with(TemporalAdjusters.firstDayOfYear()).format(formatter);
			} else {
				return null;
			}
		}
		
		List<Map<String, Object>> list = noteDao.selectMaps(MyWrapper.of(BcNote.class)
				.select("a.realname, COUNT(DISTINCT a.projectid) AS 'task'")
				.from("a")
				.ne(BcNote::getProjectId, 0)
				.eq(BcNote::getProjectType, reqVi.getProjectType())
				.between(BcNote::getSubmitDate, start, end)
				.groupBy(BcNote::getAccount)
				.orderByDesc("task")
				.limit(20));
		
		List<BoardCount> counts = new ArrayList<>(list.size());
		for ( Map<String, Object> map : list ) {
			BoardCount count = new BoardCount();
			counts.add(count);
			count.setRealname(ObjectUtil.getString(map,"realname"));
			count.setCount(ObjectUtil.getLong(map, "task"));
		}
		
		BoardCountVo resp = new BoardCountVo();
		resp.setStart(start);
		resp.setEnd(end);
		resp.setList(counts);
		
		return resp;
	}
}
