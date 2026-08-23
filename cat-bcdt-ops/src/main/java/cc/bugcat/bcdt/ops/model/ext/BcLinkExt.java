package cc.bugcat.bcdt.ops.model.ext;

import java.lang.String;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import cc.bugcat.acore.framework.entity.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * bc_link:超链接按钮
 * @author auto
 * 2026-08-02 11:45:48
 * */
@Getter
@Setter
public class BcLinkExt extends Model {

	/**
	 * 自动生成代码，禁止手动修改。枚举、自定义属性，请在子类中添加！！！
	 * */

    @TableId(value = "id", type = IdType.AUTO)
	private String id;	// 数据id

    @TableField("`type`")
	private String type;	// 数据类型：text；link

    @TableField("`label`")
	private String label;	// 显示内容

    @TableField("`title`")
	private String title;	// 链接title

    @TableField("`linkurl`")
	private String linkUrl;	// 超链接url

    @TableField("`wintitle`")
	private String winTitle;	// 新开窗口标题

    @TableField("`classname`")
	private String className;	// 样式名

    @TableField("`acs`")
	private String acs;	// 权限

    @TableField("`desc`")
	private String desc;	// 描述

    @TableField("`extra`")
	private String extra;	// html扩展

    @TableField("`pageid`")
	private String pageId;	// 所属页面

    @TableField("`rowid`")
	private String rowId;	// 所属行

    @TableField("`active`")
	private String active;	// 是否有效


	/**
	 * 传入属性名，获取到对象属性值
	 * */
	public Object theValue(String property){
		Object result = null;
		switch ( property ){
			case "id":
				result = this.getId();
				break;
			case "type":
				result = this.getType();
				break;
			case "label":
				result = this.getLabel();
				break;
			case "title":
				result = this.getTitle();
				break;
			case "linkUrl":
				result = this.getLinkUrl();
				break;
			case "winTitle":
				result = this.getWinTitle();
				break;
			case "className":
				result = this.getClassName();
				break;
			case "acs":
				result = this.getAcs();
				break;
			case "desc":
				result = this.getDesc();
				break;
			case "extra":
				result = this.getExtra();
				break;
			case "pageId":
				result = this.getPageId();
				break;
			case "rowId":
				result = this.getRowId();
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