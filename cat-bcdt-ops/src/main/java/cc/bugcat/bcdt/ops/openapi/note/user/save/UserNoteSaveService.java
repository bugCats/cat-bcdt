package cc.bugcat.bcdt.ops.openapi.note.user.save;


import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.bcdt.ops.dao.zentao.BcNoteDao;
import cc.bugcat.bcdt.ops.model.zentao.BcNote;
import cc.bugcat.bcdt.ops.openapi.note.user.save.vi.UserNoteSaveVi;
import cc.bugcat.bcdt.ops.openapi.note.user.save.vi.ZentaoProject;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.service.inter.ZenTaoService;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class UserNoteSaveService extends AbstractOpenApiService<UserNoteSaveVi, Void> {
    
    @Autowired
    private BcNoteDao noteDao;
    
    @Autowired
    private ZenTaoService zenTaoService;
    
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Void execute(UserNoteSaveVi req, RequestContext context) {
        UserInfo user = UserUtil.getUser(context);
        Date now = new Date();
        
        List<ZentaoProject> list = req.getList();
        for( ZentaoProject rp : list ){
            if( BooleanUtils.isTrue(rp.getRemove()) ){
                noteDao.deleteById(rp.getId());
                continue;
            }
            
            BcNote note = BeanCopyUtil.copy(rp, BcNote::new);
            note.setAccount(user.getAccount());
            note.setRealname(user.getRealname());
            note.setSubmitDate(req.getSubmitDate());
            note.setSaveDate(now);
            
            if( note.getId()  > 0 ){
                noteDao.updateById(note);
            } else {
                note.setId(null);
                noteDao.insert(note);
            }
        }
        
        return null;
    }
}
