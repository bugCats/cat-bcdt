package cc.bugcat.bcdt.ops.openapi.note.team.day;


import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.dao.zentao.BcNoteDao;
import cc.bugcat.bcdt.ops.model.zentao.BcNote;
import cc.bugcat.bcdt.ops.openapi.note.team.day.vo.TeamDayNoteVo;
import cc.bugcat.bcdt.ops.openapi.note.user.day.vo.UserDayNote;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class TeamDayNoteService extends AbstractOpenApiService<String, List<TeamDayNoteVo>> {
    
    @Autowired
    private BcNoteDao noteDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<TeamDayNoteVo> execute(String date, RequestContext context) {
        UserInfo user = UserUtil.getUser(context);
        
        if( StringUtils.isBlank(date) ){
            date = DateUtil.ss.getDate();
        }
        
        String uid = user.getAccount();
        if ( Opers.hasOper(user, Opers.teamNote) ) {
            uid = null;
        }
        
        List<BcNote> list = noteDao.selectList(MyWrapper.of(BcNote.class)
                .eq(BcNote::getSubmitDate, date)
                .eq(BcNote::getAccount, uid));
        
        Map<String, List<BcNote>> listMap = list.stream().collect(Collectors.groupingBy(BcNote::getAccount));
        
        List<TeamDayNoteVo> result = new ArrayList<>(listMap.size());
        listMap.forEach((key, value) -> {
            TeamDayNoteVo vo = new TeamDayNoteVo();
            value.sort((o1, o2) -> Long.compare(o2.getId(), o1.getId()));
            vo.setList(BeanCopyUtil.copys(value, UserDayNote::new));
            vo.setRealname(key);
            result.add(vo);
        });
        
        Collator collator = Collator.getInstance(Locale.CHINA);
        result.sort((o1, o2) -> collator.compare(o1.getRealname(), o2.getRealname()));
        return result;
        
    }
}
