package cc.bugcat.bcdt.ops.openapi.note.team.month;

import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.ops.dao.zentao.BcNoteDao;
import cc.bugcat.bcdt.ops.model.zentao.BcNote;
import cc.bugcat.bcdt.ops.openapi.note.team.month.vo.TeamMonthNoteVo;
import cc.bugcat.bcdt.ops.openapi.note.team.month.vo.UserDay;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Collator;
import java.util.*;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class TeamMonthNoteService extends AbstractOpenApiService<String, TeamMonthNoteVo> {
    
    @Autowired
    private BcNoteDao noteDao;
    
    
    @Override
    @Transactional(readOnly = true)
    public TeamMonthNoteVo execute(String date, RequestContext context) {
        
        Date now = null;
        if( StringUtils.isBlank(date) ){
            now = new Date();
        } else {
            now = DateUtil.ss.parseDate(date);
        }
        
        String start = DateUtil.rs.startMonth(now);
        String end = DateUtil.rs.endMonth(now);
        
        List<BcNote> list = noteDao.selectList(MyWrapper.of(BcNote.class)
                .select(BcNote::getSubmitDate, BcNote::getAccount, BcNote::getRealname)
                .between(BcNote::getSubmitDate, start, end));
        
        Map<UserKey, Set<String>> dateMap = new HashMap<>();
        for( BcNote note : list){
            UserKey userKey = new UserKey(note.getAccount(), note.getRealname());
            Set<String> dates = dateMap.get(userKey);
            if( dates == null ){
                dates = new HashSet<>();
                dateMap.put(userKey, dates);
            }
            dates.add(note.getSubmitDate());
        }
        
        List<String> monthDate = DateUtil.rs.getRangeDate(start, end, "lr");
        
        List<UserDay> userDays = new ArrayList<>(dateMap.size());
        for(Map.Entry<UserKey, Set<String>> entry : dateMap.entrySet()){
            UserKey emp = entry.getKey();
            Set<String> dates = entry.getValue();
            
            List<Boolean> notes = new ArrayList<>(monthDate.size());
            for ( String day : monthDate ) {
                if ( dates.contains(day) ) {
                    notes.add(Boolean.TRUE);
                } else {
                    notes.add(Boolean.FALSE);
                }
            }
            
            UserDay vo = new UserDay();
            vo.setAccount(emp.account);
            vo.setRealname(emp.realname);
            vo.setDates(notes);
            userDays.add(vo);
        }
        
        Collator collator = Collator.getInstance(Locale.CHINA);
        userDays.sort((o1, o2) -> collator.compare(o1.getRealname(), o2.getRealname()));
        
        TeamMonthNoteVo resp = new TeamMonthNoteVo();
        resp.setMonth(DateUtil.rs.format(now, "yyyy-MM"));
        resp.setDates(monthDate);
        resp.setUserDays(userDays);
        
        return resp;
    }
    
    
    
    
    private static class UserKey {
        
        private final String account;
        private final String realname;

        private UserKey(String account, String realname) {
            this.account = account;
            this.realname = realname;
        }
        
        @Override
        public boolean equals(Object other) {
            if ( this == other ) {
                return true;
            }
            if ( other == null || getClass() != other.getClass() ) {
                return false;
            }
            UserKey emp = (UserKey) other;
            return account.equals(emp.account);
        }
        
        @Override
        public int hashCode() {
            return account.hashCode();
        }
    }
}
