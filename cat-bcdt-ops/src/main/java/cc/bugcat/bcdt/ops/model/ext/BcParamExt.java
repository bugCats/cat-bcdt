package cc.bugcat.bcdt.ops.model.ext;

import java.lang.String;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import cc.bugcat.acore.framework.entity.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * bc_param:参数表
 * @author auto
 * 2026-07-30 16:05:07
 * */
@Getter
@Setter
public class BcParamExt extends Model {

	/**
	 * 自动生成代码，禁止手动修改。枚举、自定义属性，请在子类中添加！！！
	 * */

    @TableId(value = "id", type = IdType.AUTO)
	private String id;	// 参数名

    @TableField("`value`")
	private String value;	// 参数值

    @TableField("`desc`")
	private String desc;	// 参数描述


	/**
	 * 传入属性名，获取到对象属性值
	 * */
	public Object theValue(String property){
		Object result = null;
		switch ( property ){
			case "id":
				result = this.getId();
				break;
			case "value":
				result = this.getValue();
				break;
			case "desc":
				result = this.getDesc();
				break;
			default:
				break;
		}
		return result;
    }

}