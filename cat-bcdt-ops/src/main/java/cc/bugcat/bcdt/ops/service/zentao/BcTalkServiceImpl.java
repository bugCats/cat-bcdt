package cc.bugcat.bcdt.ops.service.zentao;

import cc.bugcat.acore.mybatis.plus.BaseService;
import cc.bugcat.bcdt.ops.dao.zentao.BcTalkDao;
import cc.bugcat.bcdt.ops.model.zentao.BcTalk;
import cc.bugcat.bcdt.ops.service.inter.BcTalkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BcTalkServiceImpl extends BaseService<BcTalkDao, BcTalk> implements BcTalkService {


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean save(BcTalk talk) {
        if( "0".equals(talk.getRead()) ){
            talk.setRead( talk.getAccount().equals(talk.getSendAccount()) ? "1" : "0" ); //自己发送的消息默认已读
        }
        return super.save(talk);
    }


    
}
