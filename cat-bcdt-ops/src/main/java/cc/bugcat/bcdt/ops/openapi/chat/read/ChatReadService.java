package cc.bugcat.bcdt.ops.openapi.chat.read;

import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.dao.zentao.BcTalkDao;
import cc.bugcat.bcdt.ops.model.zentao.BcTalk;
import cc.bugcat.bcdt.ops.openapi.chat.read.vi.ChatReadVi;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class ChatReadService extends AbstractOpenApiService<ChatReadVi, Void> {
    
    @Autowired
    private BcTalkDao talkDao;
    
    
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Void execute(ChatReadVi reqVi, RequestContext context) {
        UserInfo user = UserUtil.getUser(context);
        talkDao.update(MyWrapper.of(BcTalk.class)
                        .set(BcTalk::getRead, "1")
                        .eq(BcTalk::getRead, "0")
                        .eq(BcTalk::getId, reqVi.getMsgId())
                        .eq(BcTalk::getAccount, user.getAccount())
                        .eq(BcTalk::getMsgType, reqVi.getMsgType()));
        return null;
    }
}
