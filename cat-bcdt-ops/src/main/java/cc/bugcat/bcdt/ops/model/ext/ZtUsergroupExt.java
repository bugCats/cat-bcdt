package cc.bugcat.bcdt.ops.model.ext;

import java.lang.String;
import java.lang.Integer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import cc.bugcat.acore.framework.entity.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * zt_usergroup:
 * @author auto
 * 2026-07-30 16:08:34
 * */
@Getter
@Setter
public class ZtUsergroupExt extends Model {

	/**
	 * 自动生成代码，禁止手动修改。枚举、自定义属性，请在子类中添加！！！
	 * */

    @TableId(value = "id", type = IdType.AUTO)
	private Integer id;	// 

    @TableField("`account`")
	private String account;	// 

    @TableField("`group`")
	private Integer group;	// 

    @TableField("`project`")
	private String project;	// 


}