package cc.bugcat.bcdt.ops.openapi.dbProxy.save.vi;

import cc.bugcat.acore.framework.utils.validate.ValidateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Schema
public class DbProxySaveVi {

    @Schema(description = "数据id")
    private Long id;

    @Schema(description = "代理名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @Schema(description = "描述")
    private String desc;

    @Schema(description = "实际数据库IP+端口")
    @NotBlank(message = "实际数据库IP+端口不能为空")
    private String hostAddr;

    @Schema(description = "绑定到本地端口")
    @NotNull(message = "本地端口不能为空")
    private Integer localPort;

    @Schema(description = "dba账号")
    @NotBlank(message = "dba账号不能为空")
    private String dbaUser;

    @Schema(description = "dba密码")
    @NotBlank(message = "dba密码不能为空")
    private String dbaPwd;

    @Schema(description = "写账号")
    private String writeUser;

    @Schema(description = "写密码")
    private String writePwd;

    @Schema(description = "只读账号")
    private String readUser;

    @Schema(description = "只读密码")
    private String readPwd;

    @Schema(description = "白名单账号")
    private String whiteUser;

    @Schema(description = "是否启用：0禁用；1启用；")
    private String enable;


    public void validate() {
        
        // 新增
        if ( id == null ) {
            if ( StringUtils.isBlank(enable) ) {
                enable = "0";
            }
            
            ValidateUtil.validate(this);
        }
    }

}
