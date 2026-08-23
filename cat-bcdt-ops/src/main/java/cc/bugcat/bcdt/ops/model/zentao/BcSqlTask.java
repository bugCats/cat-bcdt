package cc.bugcat.bcdt.ops.model.zentao;

import cc.bugcat.bcdt.ops.model.ext.BcSqlTaskExt;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * BcSqlTaskExt 扩展类
 * bc_sql_task:SQL计划任务
 * @author auto
 * 2026-07-30 16:05:40
 * */
@Getter
@Setter
@TableName("bc_sql_task")
public class BcSqlTask extends BcSqlTaskExt {

}
