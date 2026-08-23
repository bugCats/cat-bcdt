package cc.bugcat.bcdt.ops.openapi.note.user.month;

import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.ops.dao.zentao.BcNoteDao;
import cc.bugcat.bcdt.ops.model.zentao.BcNote;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class UserMonthNoteService extends AbstractOpenApiService<String, List<String>> {
    
    @Autowired
    private BcNoteDao noteDao;
    
    
    @Override
    @Transactional(readOnly = true)
    public List<String> execute(String date, RequestContext context) {
        UserInfo user = UserUtil.getUser(context);
        
        Date now = null;
        if( StringUtils.isBlank(date) ){
            now = new Date();
        } else {
            now = DateUtil.rd.parseDate(date, DateUtil.yMd);
        }
        
        String start = DateUtil.rs.startMonth(now);
        String end = DateUtil.rs.endMonth(now);
        
        List<String> list = noteDao.selectString(MyWrapper.of(BcNote.class)
                        .select(BcNote::getSubmitDate)
                        .distinct()
                        .eq(BcNote::getAccount, user.getAccount())
                        .between(BcNote::getSubmitDate, start, end)
                        .orderByAsc(BcNote::getSubmitDate));
        
        return list;
    }
}
