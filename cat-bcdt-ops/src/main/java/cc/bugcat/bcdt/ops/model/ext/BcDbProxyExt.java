package cc.bugcat.bcdt.ops.model.ext;

import java.lang.Long;
import java.lang.String;
import java.lang.Integer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import cc.bugcat.acore.framework.entity.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * bc_db_proxy:数据库代理配置
 * @author auto
 * 2026-08-11 10:04:50
 * */
@Getter
@Setter
public class BcDbProxyExt extends Model {

	/**
	 * 自动生成代码，禁止手动修改。枚举、自定义属性，请在子类中添加！！！
	 * */

    @TableId(value = "id", type = IdType.AUTO)
	private Long id;	// 数据id

    @TableField("`name`")
	private String name;	// 代理名称

    @TableField("`desc`")
	private String desc;	// 描述

    @TableField("`hostaddr`")
	private String hostAddr;	// 实际数据库IP+端口

    @TableField("`localport`")
	private Integer localPort;	// 绑定到本地端口

    @TableField("`dbauser`")
	private String dbaUser;	// dba账号

    @TableField("`dbapwd`")
	private String dbaPwd;	// dba密码

    @TableField("`writeuser`")
	private String writeUser;	// 写账号

    @TableField("`writepwd`")
	private String writePwd;	// 写密码

    @TableField("`readuser`")
	private String readUser;	// 只读账号

    @TableField("`readpwd`")
	private String readPwd;	// 只读密码

    @TableField("`whiteuser`")
	private String whiteUser;	// 白名单账号

    @TableField("`enable`")
	private String enable;	// 是否启用：0禁用；1启用；


	/**
	 * 传入属性名，获取到对象属性值
	 * */
	public Object theValue(String property){
		Object result = null;
		switch ( property ){
			case "id":
				result = this.getId();
				break;
			case "name":
				result = this.getName();
				break;
			case "desc":
				result = this.getDesc();
				break;
			case "hostAddr":
				result = this.getHostAddr();
				break;
			case "localPort":
				result = this.getLocalPort();
				break;
			case "dbaUser":
				result = this.getDbaUser();
				break;
			case "dbaPwd":
				result = this.getDbaPwd();
				break;
			case "writeUser":
				result = this.getWriteUser();
				break;
			case "writePwd":
				result = this.getWritePwd();
				break;
			case "readUser":
				result = this.getReadUser();
				break;
			case "readPwd":
				result = this.getReadPwd();
				break;
			case "whiteUser":
				result = this.getWhiteUser();
				break;
			case "enable":
				result = this.getEnable();
				break;
			default:
				break;
		}
		return result;
    }

}