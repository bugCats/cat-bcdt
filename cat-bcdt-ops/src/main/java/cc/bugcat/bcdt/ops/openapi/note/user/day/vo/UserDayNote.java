package cc.bugcat.bcdt.ops.openapi.note.user.day.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.Date;


@Data
@Schema
public class UserDayNote {

    @Schema(description = "id")
    private Long id;	// id
    
    @Schema(description = "姓名")
    private String realname;

    @Schema(description = "提交时间yyyy-mm-dd")
    private String submitDate;	// 提交时间yyyy-mm-dd

    @Schema(description = "内容")
    private String context;	// 内容
    
    @Schema(description = "耗时")
    private Double costTime;	// 耗时

    @Schema(description = "保存时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date savedate;	// 保存时间
    
    @Schema(description = "项目、任务主键")
    private Long projectId;	// 项目、任务id

    @Schema(description = "项目、任务名称")
    private String projectName;	// 项目、任务名称

    @Schema(description = "项目、任务类型：10 zt_task；20 zt_bug")
    private Integer projectType;


}
