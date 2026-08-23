package cc.bugcat.bcdt.ops.model.ext;

import java.lang.Long;
import java.lang.Double;
import java.util.Date;
import java.lang.String;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import cc.bugcat.acore.framework.entity.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * bc_note:个人日报
 * @author auto
 * 2026-07-30 16:04:54
 * */
@Getter
@Setter
public class BcNoteExt extends Model {

	/**
	 * 自动生成代码，禁止手动修改。枚举、自定义属性，请在子类中添加！！！
	 * */

    @TableId(value = "id", type = IdType.AUTO)
	private Long id;	// 数据id

    @TableField("`account`")
	private String account;	// 账号

    @TableField("`realname`")
	private String realname;	// 姓名

    @TableField("`submitdate`")
	private String submitDate;	// 提交时间yyyy-mm-dd

    @TableField("`context`")
	private String context;	// 内容

    @TableField("`projectid`")
	private Long projectId;	// 项目、任务id

    @TableField("`projecttype`")
	private Integer projectType;	// 10 zt_task；20 zt_bug

    @TableField("`projectname`")
	private String projectName;	// 项目名称

    @TableField("`costtime`")
	private Double costTime;	// 耗时

    @TableField("`costorg`")
	private String costOrg;	// 开发成本归属组织

    @TableField("`coststatus`")
	private String costStatus;	// 成本是否需要统计：1需要；0不需要；

    @TableField("`savedate`")
	private Date saveDate;	// 最后修改时间


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
			case "submitDate":
				result = this.getSubmitDate();
				break;
			case "context":
				result = this.getContext();
				break;
			case "projectId":
				result = this.getProjectId();
				break;
			case "projectType":
				result = this.getProjectType();
				break;
			case "projectName":
				result = this.getProjectName();
				break;
			case "costTime":
				result = this.getCostTime();
				break;
			case "costOrg":
				result = this.getCostOrg();
				break;
			case "costStatus":
				result = this.getCostStatus();
				break;
			case "saveDate":
				result = this.getSaveDate();
				break;
			default:
				break;
		}
		return result;
    }

}