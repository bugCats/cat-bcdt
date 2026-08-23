package cc.bugcat.bcdt.ops.openapi.chat.list;

import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.ops.dao.zentao.BcTalkDao;
import cc.bugcat.bcdt.ops.model.zentao.BcTalk;
import cc.bugcat.bcdt.ops.openapi.chat.list.vi.ChatListVi;
import cc.bugcat.bcdt.ops.openapi.chat.list.vo.ChatListVo;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class ChatListService extends AbstractOpenApiService<ChatListVi, List<ChatListVo>> {
    
    
    @Autowired
    private BcTalkDao talkDao;
    
    
    @Override
    @Transactional(readOnly = true)
    public List<ChatListVo> execute(ChatListVi reqVi, RequestContext context) {
        
        UserInfo user = UserUtil.getUser(context);
        
        Date now = new Date();
        int dateRange = reqVi.getDateRange() != null ? reqVi.getDateRange() : 7;

        BcTalk query = new BcTalk();
        query.setAccount(user.getAccount());
        query.setId(reqVi.getMinId());
        query.setCount(reqVi.getPageSize());
        query.setMsgType(reqVi.getMsgType());
        query.setMsgTags(reqVi.getMsgTags());
        query.setRead(reqVi.getRead());
        query.setDateStart(DateUtil.rd.endDateTime(DateUtil.rd.plusDay(now, -dateRange)));
        query.setDateEnd(DateUtil.rd.endDateTime(now));
        
        List<BcTalk> talks = talkDao.selectList(MyWrapper.of(BcTalk.class)
                        .in(BcTalk::getAccount, user.getAccount(), "---")
                        .lt(query::getId)
                        .eq(query::getMsgType)
                        .between(BcTalk::getSaveDate, query.getDateStart(), query.getDateEnd())
                        .eq(query::getMsgTags)
                        .eq(query::getRead)
                        .orderByDesc(BcTalk::getId)
                        .limit(query.getCount()));
        
        List<ChatListVo> list = Optional.ofNullable(talks)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(talk -> {
                    ChatListVo item = BeanCopyUtil.copy(talk, ChatListVo::new);
                    String text = StringUtils.trimToEmpty(item.getContent());
                    int start = 0;
                    while ( start <= text.length() ) {
                        start = text.indexOf("<img", start);
                        if ( start < 0 ) {
                            break;
                        }
                        int end = text.indexOf("/>", start);
                        if ( end < 0 ) {
                            break;
                        }
                        item.setContent(text.substring(0, start) + text.substring(end + 2));
                        start = end + 2;
                    }
                    return item;
                })
                .sorted(Comparator.comparingLong(ChatListVo::getId))
                .collect(Collectors.toList());
        
        return list;
        
    }
}
