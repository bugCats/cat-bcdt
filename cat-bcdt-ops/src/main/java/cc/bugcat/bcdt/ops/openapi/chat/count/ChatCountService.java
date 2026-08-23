package cc.bugcat.bcdt.ops.openapi.chat.count;

import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.ops.dao.zentao.BcTalkDao;
import cc.bugcat.bcdt.ops.model.zentao.BcTalk;
import cc.bugcat.bcdt.ops.openapi.chat.count.vi.ChatCountVi;
import cc.bugcat.bcdt.ops.openapi.chat.count.vo.ChatCountVo;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class ChatCountService extends AbstractOpenApiService<ChatCountVi, List<ChatCountVo>> {
    
    
    @Autowired
    private BcTalkDao talkDao;
    
    
    @Override
    @Transactional(readOnly = true)
    public List<ChatCountVo> execute(ChatCountVi reqVi, RequestContext context) {
        
        UserInfo user = UserUtil.getUser(context);
        Date now = new Date();
        int dateRange = reqVi.getDateRange() != null ? reqVi.getDateRange() : 7;
        
        BcTalk query = new BcTalk();
        query.setAccount(user.getAccount());
        query.setDateStart(DateUtil.rd.startDateTime(DateUtil.rd.plusDay(now, -dateRange)));
        query.setDateEnd(DateUtil.rd.endDateTime(now));
        
        List<BcTalk> list = talkDao.selectList(MyWrapper.of(BcTalk.class)
                .select(BcTalk::getMsgType).select("COUNT(1) AS 'total'", "SUM(IF(a.read = '0', 1, 0)) AS 'unread'")
                        .eq(BcTalk::getAccount, user.getAccount())
                        .between(BcTalk::getSaveDate, query.getDateStart(), query.getDateEnd())
                        .groupBy(BcTalk::getMsgType)
                );
        
        List<ChatCountVo> result = BeanCopyUtil.copys(list, ChatCountVo::new);
        Optional.ofNullable(result).orElseGet(Collections::emptyList).forEach(msg -> {
            msg.setRead(msg.getTotal() - msg.getUnread());
        });
        
        
        return result;
        
    }
}
