package cc.bugcat.bcdt.ops.service.zentao;


import cc.bugcat.acore.mybatis.plus.BaseService;
import cc.bugcat.acore.framework.utils.SqlFactory;
import cc.bugcat.bcdt.ops.dao.zentao.BcSqlDao;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;
import cc.bugcat.bcdt.ops.model.zentao.BcSql;
import cc.bugcat.bcdt.ops.service.inter.BcSqlService;
import cc.bugcat.bcdt.ops.utils.JDBCUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;

@Service
public class BcSqlServiceImpl extends BaseService<BcSqlDao, BcSql> implements BcSqlService {

    private static final Logger logger = LoggerFactory.getLogger(BcSqlServiceImpl.class);
    
    
    @Override
    public String executeSql(BcSql bcSql, BcDbSet dbSet){
        String err = "";
        try (Connection conn = JDBCUtil.getConn(dbSet)) {
            bcSql.setStatus("20"); //执行成功
            
            SqlFactory factory = new SqlFactory(conn, false);
            factory.setSql(bcSql.getSql());
            
            int res = factory.execute();
            factory.commit();
            
            logger.info("执行SQL ==> {}", factory.getSql() + " 影响行数：" + res);
            logger.info("影响行数：{}", res);
        
        } catch (Exception ex) {
            bcSql.setStatus("10"); //执行失败了
            err = "执行sql异常：" + ex.getMessage();
        }
        return err;
    }
    
    
}
