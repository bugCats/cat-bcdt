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
 * bc_db_auth:数据库申请授权记录表
 * @author auto
 * 2026-07-30 16:01:42
 * */
@Getter
@Setter
public class BcDbAuthExt extends Model {

	/**
	 * 自动生成代码，禁止手动修改。枚举、自定义属性，请在子类中添加！！！
	 * */

    @TableId(value = "id", type = IdType.AUTO)
	private Long id;	// 数据id

    @TableField("`account`")
	private String account;	// 账号

    @TableField("`realname`")
	private String realname;	// 姓名

    @TableField("`ipaddr`")
	private String ipAddr;	// 申请时ip

    @TableField("`reason`")
	private String reason;	// 申请原因

    @TableField("`path`")
	private String path;	// 日志目录

    @TableField("`createdate`")
	private Date createDate;	// 创建时间


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
			case "ipAddr":
				result = this.getIpAddr();
				break;
			case "reason":
				result = this.getReason();
				break;
			case "path":
				result = this.getPath();
				break;
			case "createDate":
				result = this.getCreateDate();
				break;
			default:
				break;
		}
		return result;
    }

}