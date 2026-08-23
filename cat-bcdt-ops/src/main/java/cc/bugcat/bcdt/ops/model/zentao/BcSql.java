package cc.bugcat.bcdt.ops.model.zentao;

import cc.bugcat.bcdt.ops.model.ext.BcSqlExt;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * BcSqlExt 扩展类
 * bc_sql:数据库版本管理
 * @author auto
 * 2026-07-30 16:05:24
 * */
@Getter
@Setter
@TableName("bc_sql")
public class BcSql extends BcSqlExt {

    private List<Long> ids;

    private Date startDate;

    private Date endDate;
    
}
