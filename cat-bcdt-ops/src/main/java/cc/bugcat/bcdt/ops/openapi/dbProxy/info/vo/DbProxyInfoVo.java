package cc.bugcat.bcdt.ops.openapi.dbProxy.info.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema
public class DbProxyInfoVo {


    @Schema(description = "数据id")
    private Long id;    

    @Schema(description = "代理名称")
    private String name;    

    @Schema(description = "描述")
    private String desc;    

    @Schema(description = "实际数据库IP+端口")
    private String hostAddr;    

    @Schema(description = "绑定到本地端口")
    private Integer localPort;    

    @Schema(description = "dba账号")
    private String dbaUser;    

    @Schema(description = "dba密码")
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


}
