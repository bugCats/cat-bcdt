package cc.bugcat.bcdt.ops.openapi.zentao.all.vi;


import cc.bugcat.acore.framework.entity.Pageable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;


@Data
@Schema
public class ZenTaoTaskVi extends Pageable {

    
    @Schema(description = "日期范围")
    @NotNull(message = "日期范围不能为空")
    private Integer dateRange;

    @Schema(description = "关键词")
    private String keyword;
    
    
    @JsonIgnore
    private Long objectId;
    @JsonIgnore
    private String account;
    @JsonIgnore
    private Date dateStart;
    @JsonIgnore
    private Date dateEnd;


}
