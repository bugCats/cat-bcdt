package cc.bugcat.bcdt.ops.openapi.note.user.save.vi;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema
public class UserNoteSaveVi {

    
    @NotBlank(message = "日期不能为空")
    @Schema(description = "日报日期")
    private String submitDate;
    
    
    @Valid
    @NotNull(message = "日报内容不能为空")
    @Schema(description = "日报")
    private List<ZentaoProject> list;


}
