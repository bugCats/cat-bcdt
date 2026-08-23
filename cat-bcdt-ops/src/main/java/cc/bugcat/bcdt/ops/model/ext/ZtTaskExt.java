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
 * zt_task:
 * @author auto
 * 2026-07-30 16:07:58
 * */
@Getter
@Setter
public class ZtTaskExt extends Model {

	/**
	 * 自动生成代码，禁止手动修改。枚举、自定义属性，请在子类中添加！！！
	 * */

    @TableId(value = "id", type = IdType.AUTO)
	private Integer id;	// 

    @TableField("`project`")
	private Integer project;	// 

    @TableField("`design`")
	private Integer design;	// 

    @TableField("`name`")
	private String name;	// 

    @TableField("`desc`")
	private String desc;	// 


    @TableField("`openedby`")
	private String openedBy;	// 

    @TableField("`openeddate`")
	private Date openedDate;	// 

    @TableField("`assignedto`")
	private String assignedTo;	// 

    @TableField("`assigneddate`")
	private Date assignedDate;	// 

    @TableField("`eststarted`")
	private Date estStarted;	// 

    @TableField("`realstarted`")
	private Date realStarted;	// 

    @TableField("`finishedby`")
	private String finishedBy;	// 

    @TableField("`finisheddate`")
	private Date finishedDate;	// 

    @TableField("`finishedlist`")
	private String finishedList;	// 

    @TableField("`canceledby`")
	private String canceledBy;	// 

    @TableField("`canceleddate`")
	private Date canceledDate;	// 

    @TableField("`closedby`")
	private String closedBy;	// 

    @TableField("`closeddate`")
	private Date closedDate;	// 

    @TableField("`planduration`")
	private Integer planDuration;	// 

    @TableField("`realduration`")
	private Integer realDuration;	// 

    @TableField("`closedreason`")
	private String closedReason;	// 

    @TableField("`lasteditedby`")
	private String lastEditedBy;	// 

    @TableField("`lastediteddate`")
	private Date lastEditedDate;	// 

    @TableField("`activateddate`")
	private Date activatedDate;	// 

    @TableField("`deleted`")
	private Integer deleted;	// 


}