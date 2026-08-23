package cc.bugcat.bcdt.ops.openapi.gener;

import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.gener.GencoderService;
import cc.bugcat.acore.gener.GencoderVo;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;
import cc.bugcat.bcdt.ops.openapi.gener.vi.GenerCoderVi;
import cc.bugcat.bcdt.ops.openapi.gener.vo.GenerCoderVo;
import cc.bugcat.bcdt.ops.service.inter.BcDbSetService;
import cc.bugcat.bcdt.ops.utils.JDBCUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class GenerCoderService extends AbstractOpenApiService<GenerCoderVi, GenerCoderVo> {

    @Autowired
    private BcDbSetService dbSetService;
    
    @Override
    @Transactional(readOnly = true)
    public GenerCoderVo execute(GenerCoderVi reqVi, RequestContext context) {
        
        BcDbSet dbSet = dbSetService.getByCode(reqVi.getDatabaseCode());
        
        try ( Connection conn = JDBCUtil.getConn(dbSet) ) {

            GencoderService service = new GencoderService(conn);

            GencoderVo resp = service.execute(reqVi);
            
            return BeanCopyUtil.copy(resp, GenerCoderVo::new);
            
        } catch ( Exception ex ) {
            throw BusinessException.builder("500", ex);
        }
        
    }
}
