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
 * bc_sql:数据库版本管理
 * @author auto
 * 2026-08-07 15:11:10
 * */
@Getter
@Setter
public class BcSqlExt extends Model {

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
	private Long dbId;	// 数据库

    @TableField("`sql`")
	private String sql;	// 执行的SQL

    @TableField("`desc`")
	private String desc;	// 描述

    @TableField("`status`")
	private String status;	// 执行状态：10 初始；90 已同步；

    @TableField("`submitaccount`")
	private String submitAccount;	// 同步人账号

    @TableField("`submitrealname`")
	private String submitRealname;	// 同步人姓名

    @TableField("`submitdate`")
	private Date submitDate;	// 同步时间


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
			case "submitAccount":
				result = this.getSubmitAccount();
				break;
			case "submitRealname":
				result = this.getSubmitRealname();
				break;
			case "submitDate":
				result = this.getSubmitDate();
				break;
			default:
				break;
		}
		return result;
    }

}