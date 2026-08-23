package cc.bugcat.bcdt.ops.openapi.dbset.save.vi;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.utils.validate.ValidateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Schema
public class DbsetSaveVi {

    @Schema(description = "数据id")
    private Long id;

    @Schema(description = "数据库名")
    @NotBlank(message = "数据库名不能为空")
    private String code;

    @Schema(description = "数据库描述")
    private String desc;

    @Schema(description = "jdbc链接：jdbc:mysql://127.0.0.1:3306/dbname")
    @NotBlank(message = "jdbc链接不能为空")
    private String jdbcUrl;

    @Schema(description = "数据库账号")
    @NotBlank(message = "数据库账号不能为空")
    private String username;

    @Schema(description = "数据库密码")
    @NotBlank(message = "数据库密码不能为空")
    private String password;

    @Schema(description = "驱动")
    @NotBlank(message = "驱动不能为空")
    private String driverName;

    @Schema(description = "数据库字段加密方式：sm4")
    private String cryptoType;

    @Schema(description = "数据库字段加密key")
    private String cryptoKey;

    
    public void validate() {
        
        // 新增
        if ( id == null ) {
            
            if ( StringUtils.isNotBlank(cryptoType) && StringUtils.isBlank(cryptoKey) ) {
                throw BusinessException.builder(Errors.ERROR_PARAM, "加密Key不能为空");
            }

            ValidateUtil.validate(this);
            
        }
        
    }
    

}
