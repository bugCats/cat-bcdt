package cc.bugcat.bcdt.ops.openapi.note.user.day;


import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.dao.zentao.BcNoteDao;
import cc.bugcat.bcdt.ops.model.zentao.BcNote;
import cc.bugcat.bcdt.ops.openapi.note.user.day.vo.UserDayNote;
import cc.bugcat.bcdt.ops.openapi.note.user.day.vo.UserDayNoteVo;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class UserDayNoteService extends AbstractOpenApiService<String, UserDayNoteVo> {
    
    @Autowired
    private BcNoteDao noteDao;
    
    
    @Override
    @Transactional(readOnly = true)
    public UserDayNoteVo execute(String date, RequestContext context) {
        
        UserInfo user = UserUtil.getUser(context);
        
        UserDayNoteVo resp = new UserDayNoteVo();
        
        if ( StringUtils.isNotBlank(date) ) {
            
            List<BcNote> list = noteDao.selectList(MyWrapper.of(BcNote.class)
                    .eq(BcNote::getAccount, user.getAccount())
                    .eq(BcNote::getSubmitDate, date)
                    .orderByAsc(BcNote::getId));
            
            if ( ObjectUtil.isNotEmpty(list) ) {
                List<UserDayNote> result = new ArrayList<>(list.size());
                Date last = null;
                for ( BcNote note : list ) {
                    UserDayNote vo = BeanCopyUtil.copy(note, UserDayNote::new);
                    if ( last == null || last.getTime() < note.getSaveDate().getTime() ) {
                        last = note.getSaveDate();
                    }
                    result.add(vo);
                }
                resp.setLastSavedate(last);
                resp.setList(result);
            }
        }
        
        return resp;
    }
}
