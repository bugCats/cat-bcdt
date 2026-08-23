package cc.bugcat.bcdt.ops.openapi.zentao.all;

import cc.bugcat.acore.framework.entity.PageInfo;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.ops.model.TaskWork;
import cc.bugcat.bcdt.ops.openapi.zentao.all.vi.ZenTaoTaskVi;
import cc.bugcat.bcdt.ops.openapi.zentao.all.vo.ZenTaoAllTaskVo;
import cc.bugcat.bcdt.ops.service.inter.ZenTaoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class ZenTaoAllTaskService extends AbstractOpenApiService<ZenTaoTaskVi, PageInfo<ZenTaoAllTaskVo>> {
    
    @Autowired
    private ZenTaoService zenTaoService;
    
    @Override
    @Transactional(readOnly = true)
    public PageInfo<ZenTaoAllTaskVo> execute(ZenTaoTaskVi reqVi, RequestContext context) {
        
        Date now = new Date();
        Date start = DateUtil.rd.plusDay(now, reqVi.getDateRange());
        Date end = DateUtil.rd.plusDay(now, reqVi.getDateRange() + 30);
        
        reqVi.setDateStart(DateUtil.rd.startDateTime(start));
        reqVi.setDateEnd(DateUtil.rd.endDateTime(end));
        if ( StringUtils.isNotBlank(reqVi.getKeyword()) ) {
            try {
                reqVi.setObjectId(Long.parseLong(reqVi.getKeyword()));
            } catch ( Exception ex ) {
                
            }
        }
        
        PageInfo<TaskWork> pageInfo = zenTaoService.findAllWork(reqVi);
        
        PageInfo<ZenTaoAllTaskVo> resp = new PageInfo<>(pageInfo);
        resp.setList(BeanCopyUtil.copys(pageInfo.getList(), ZenTaoAllTaskVo::new));
        return resp;
    }
}
