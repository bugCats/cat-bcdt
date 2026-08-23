package cc.bugcat.bcdt.ops.openapi.dbtask.list.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.Date;

@Data
@Schema
public class DbtaskListVo {
    
    
    @Schema(description = "主键")
    private Long id;	// 主键
    
    @Schema(description = "创建人账号")
    private String account;	// 创建人账号
    
    @Schema(description = "创建人姓名")
    private String realname;	// 创建人姓名
    
    @Schema(description = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createDate;	// 创建时间
    
    @Schema(description = "数据库id")
    private Long dbId;	// 数据库

    @Schema(description = "数据库名")
    private String dbCode;

    @Schema(description = "数据库描述")
    private String dbDesc;
    
    @Schema(description = "执行的sql语句")
    private String sql;	// 执行的sql语句
    
    @Schema(description = "状态")
    private Integer status;
    
    @Schema(description = "SQL描述")
    private String desc;
    
    @Schema(description = "执行结果")
    private String result;
    
    @Schema(description = "计划执行时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date taskDate;
    
    @Schema(description = "执行时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date executeDate;
    
}
