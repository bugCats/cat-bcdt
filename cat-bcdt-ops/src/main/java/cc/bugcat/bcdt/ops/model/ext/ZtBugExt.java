package cc.bugcat.bcdt.ops.model.ext;

import java.util.Date;
import java.lang.String;
import java.lang.Integer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import cc.bugcat.acore.framework.entity.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * zt_bug:
 * @author auto
 * 2026-07-30 16:06:30
 * */
@Getter
@Setter
public class ZtBugExt extends Model {

	/**
	 * 自动生成代码，禁止手动修改。枚举、自定义属性，请在子类中添加！！！
	 * */

    @TableId(value = "id", type = IdType.AUTO)
	private Integer id;	// 

    @TableField("`project`")
	private Integer project;	// 

    @TableField("`product`")
	private Integer product;	// 

    @TableField("`branch`")
	private Integer branch;	// 

    @TableField("`module`")
	private Integer module;	// 

    @TableField("`story`")
	private Integer story;	// 

    @TableField("`task`")
	private Integer task;	// 

    @TableField("`totask`")
	private Integer toTask;	// 

    @TableField("`tostory`")
	private Integer toStory;	// 

    @TableField("`title`")
	private String title;	// 

    @TableField("`status`")
	private String status;	// 

    @TableField("`substatus`")
	private String subStatus;	// 

    @TableField("`confirmed`")
	private Integer confirmed;	// 

    @TableField("`activatedcount`")
	private Integer activatedCount;	// 

    @TableField("`activateddate`")
	private Date activatedDate;	// 

    @TableField("`feedbackby`")
	private String feedbackBy;	// 

    @TableField("`notifyemail`")
	private String notifyEmail;	// 

    @TableField("`mailto`")
	private String mailto;	// 

    @TableField("`openedby`")
	private String openedBy;	// 

    @TableField("`openeddate`")
	private Date openedDate;	// 

    @TableField("`openedbuild`")
	private String openedBuild;	// 

    @TableField("`assignedto`")
	private String assignedTo;	// 

    @TableField("`assigneddate`")
	private Date assignedDate;	// 

    @TableField("`deadline`")
	private Date deadline;	// 

    @TableField("`resolvedby`")
	private String resolvedBy;	// 

    @TableField("`resolution`")
	private String resolution;	// 

    @TableField("`resolvedbuild`")
	private String resolvedBuild;	// 

    @TableField("`resolveddate`")
	private Date resolvedDate;	// 

    @TableField("`closedby`")
	private String closedBy;	// 

    @TableField("`closeddate`")
	private Date closedDate;	// 

    @TableField("`duplicatebug`")
	private Integer duplicateBug;	// 

    @TableField("`relatedbug`")
	private String relatedBug;	// 


}