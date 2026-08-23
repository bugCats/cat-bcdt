package cc.bugcat.bcdt.ops.model.ext;

import java.lang.Long;
import java.lang.String;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import cc.bugcat.acore.framework.entity.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * bc_db_set:数据库配置
 * @author auto
 * 2026-08-07 10:39:35
 * */
@Getter
@Setter
public class BcDbSetExt extends Model {

	/**
	 * 自动生成代码，禁止手动修改。枚举、自定义属性，请在子类中添加！！！
	 * */

    @TableId(value = "id", type = IdType.AUTO)
	private Long id;	// 数据id

    @TableField("`code`")
	private String code;	// 数据库名

    @TableField("`desc`")
	private String desc;	// 数据库描述

    @TableField("`jdbcurl`")
	private String jdbcUrl;	// jdbc链接：jdbc:mariadb:

    @TableField("`username`")
	private String username;	// 数据库账号

    @TableField("`password`")
	private String password;	// 数据库密码

    @TableField("`drivername`")
	private String driverName;	// 驱动

    @TableField("`cryptotype`")
	private String cryptoType;	// 数据库字段加密方式：sm4

    @TableField("`cryptokey`")
	private String cryptoKey;	// 数据库字段加密key

    @TableField("`active`")
	private String active;	// 是否有效：1有效；0无效；


	/**
	 * 传入属性名，获取到对象属性值
	 * */
	public Object theValue(String property){
		Object result = null;
		switch ( property ){
			case "id":
				result = this.getId();
				break;
			case "code":
				result = this.getCode();
				break;
			case "desc":
				result = this.getDesc();
				break;
			case "jdbcUrl":
				result = this.getJdbcUrl();
				break;
			case "username":
				result = this.getUsername();
				break;
			case "password":
				result = this.getPassword();
				break;
			case "driverName":
				result = this.getDriverName();
				break;
			case "cryptoType":
				result = this.getCryptoType();
				break;
			case "cryptoKey":
				result = this.getCryptoKey();
				break;
			case "active":
				result = this.getActive();
				break;
			default:
				break;
		}
		return result;
    }

}