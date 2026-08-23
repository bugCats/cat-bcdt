package cc.bugcat.bcdt.ops.openapi.dbsql.edit;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.model.zentao.BcSql;
import cc.bugcat.bcdt.ops.openapi.dbsql.edit.vi.DbsqlEditVi;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.service.inter.BcSqlService;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class DbsqlEditService extends AbstractOpenApiService<DbsqlEditVi, Void> {
    
    
    @Autowired
    private BcSqlService sqlService;
    
    
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public Void execute(DbsqlEditVi reqVi, RequestContext context) {
        
        UserInfo user = UserUtil.getUser(context);
        
        BcSql bcSql = sqlService.getById(reqVi.getId());

        BcSql uper = new BcSql();
        uper.setId(reqVi.getId());
        
        if( "2".equals(reqVi.getType()) ){ //删除，只有10才能删除
            if( "90".equals(bcSql.getStatus()) ){
                throw BusinessException.builder("500", Errors.SYS_ERR, "已同步的数据不能删除！");
            }
            sqlService.removeById(reqVi.getId());
            
        } else if( "3".equals(reqVi.getType()) ){ // 提交
            uper.setStatus("90");
            uper.setSubmitAccount(user.getAccount());
            uper.setSubmitRealname(user.getRealname());
            uper.setSubmitDate(new Date());
            sqlService.updateById(uper);
        }
        
        return null;
    }
}
