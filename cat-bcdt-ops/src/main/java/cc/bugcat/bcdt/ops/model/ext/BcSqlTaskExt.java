package cc.bugcat.bcdt.ops.model.ext;

import java.lang.Long;
import java.util.Date;
import java.lang.String;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import cc.bugcat.acore.framework.entity.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * bc_sql_task:SQL计划任务
 * @author auto
 * 2026-08-10 09:52:29
 * */
@Getter
@Setter
public class BcSqlTaskExt extends Model {

	/**
	 * 自动生成代码，禁止手动修改。枚举、自定义属性，请在子类中添加！！！
	 * */

    @TableId(value = "id", type = IdType.AUTO)
	private Long id;	// 数据id

    @TableField("`account`")
	private String account;	// 创建人账号

    @TableField("`realname`")
	private String realname;	// 创建人姓名

    @TableField("`createdate`")
	private Date createDate;	// 创建时间

    @TableField("`dbid`")
	private Long dbId;	// 数据库名

    @TableField("`sql`")
	private String sql;	// 待执行的SQL

    @TableField("`desc`")
	private String desc;	// 描述

    @TableField("`status`")
	private String status;	// 状态：10初始；50已执行；90 执行失败

    @TableField("`taskdate`")
	private Date taskDate;	// 计划执行时间

    @TableField("`result`")
	private String result;	// 执行结果描述

    @TableField("`executedate`")
	private Date executeDate;	// 实际执行时间


	/**
	 * 传入属性名，获取到对象属性值
	 * */
	public Object theValue(String property){
		Object result = null;
		switch ( property ){
			case "id":
				result = this.getId();
				break;
			case "account":
				result = this.getAccount();
				break;
			case "realname":
				result = this.getRealname();
				break;
			case "createDate":
				result = this.getCreateDate();
				break;
			case "dbId":
				result = this.getDbId();
				break;
			case "sql":
				result = this.getSql();
				break;
			case "desc":
				result = this.getDesc();
				break;
			case "status":
				result = this.getStatus();
				break;
			case "taskDate":
				result = this.getTaskDate();
				break;
			case "result":
				result = this.getResult();
				break;
			case "executeDate":
				result = this.getExecuteDate();
				break;
			default:
				break;
		}
		return result;
    }

}