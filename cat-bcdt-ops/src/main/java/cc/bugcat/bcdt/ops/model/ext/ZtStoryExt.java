package cc.bugcat.bcdt.ops.model.ext;

import cc.bugcat.acore.framework.entity.Model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * zt_story:
 * @author auto
 * 2026-07-30 16:07:37
 * */
@Getter
@Setter
public class ZtStoryExt extends Model {

	/**
	 * 自动生成代码，禁止手动修改。枚举、自定义属性，请在子类中添加！！！
	 * */

    @TableId(value = "id", type = IdType.AUTO)
	private Integer id;	// 

    @TableField("`root`")
	private Integer root;	// 

    @TableField("`grade`")
	private Integer grade;	// 

    @TableField("`product`")
	private Integer product;	// 

    @TableField("`branch`")
	private Integer branch;	// 

    @TableField("`module`")
	private Integer module;	// 

    @TableField("`plan`")
	private String plan;	// 

    @TableField("`title`")
	private String title;	// 

    @TableField("`status`")
	private String status;	// 

    @TableField("`substatus`")
	private String subStatus;	// 

    @TableField("`openedby`")
	private String openedBy;	// 

    @TableField("`openeddate`")
	private Date openedDate;	// 

    @TableField("`assignedto`")
	private String assignedTo;	// 

    @TableField("`assigneddate`")
	private Date assignedDate;	// 

    @TableField("`approveddate`")
	private Date approvedDate;	// 

    @TableField("`lasteditedby`")
	private String lastEditedBy;	// 

    @TableField("`lastediteddate`")
	private Date lastEditedDate;	// 

    @TableField("`changedby`")
	private String changedBy;	// 

    @TableField("`changeddate`")
	private Date changedDate;	// 

    @TableField("`reviewedby`")
	private String reviewedBy;	// 

    @TableField("`revieweddate`")
	private Date reviewedDate;	// 

    @TableField("`releaseddate`")
	private Date releasedDate;	// 

    @TableField("`closedby`")
	private String closedBy;	// 

    @TableField("`closeddate`")
	private Date closedDate;	// 

    @TableField("`closedreason`")
	private String closedReason;	// 

    @TableField("`activateddate`")
	private Date activatedDate;	// 

    @TableField("`tobug`")
	private Integer toBug;	// 


}