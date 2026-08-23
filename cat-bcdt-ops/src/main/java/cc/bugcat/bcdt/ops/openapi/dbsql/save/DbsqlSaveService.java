package cc.bugcat.bcdt.ops.openapi.dbsql.save;

import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.model.zentao.BcSql;
import cc.bugcat.bcdt.ops.openapi.dbsql.save.vi.DbsqlSaveVi;
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
public class DbsqlSaveService extends AbstractOpenApiService<DbsqlSaveVi, Void> {
    
    @Autowired
    private BcSqlService sqlService;
    
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    public Void execute(DbsqlSaveVi reqVi, RequestContext context) {
        UserInfo user = UserUtil.getUser(context);
        
        BcSql bcSql = new BcSql();
        bcSql.setAccount(user.getAccount());
        bcSql.setRealname(user.getRealname());
        bcSql.setCreateDate(new Date());
        bcSql.setSql(reqVi.getSql());
        bcSql.setDesc(reqVi.getDesc());
        bcSql.setDbId(reqVi.getDbId());
        sqlService.save(bcSql);
        
        return null;
    }
}
