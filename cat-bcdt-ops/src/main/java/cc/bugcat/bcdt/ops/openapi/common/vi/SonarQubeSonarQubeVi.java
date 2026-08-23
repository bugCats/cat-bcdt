package cc.bugcat.bcdt.ops.openapi.common.vi;


import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;


@Data
@Schema
public class SonarQubeSonarQubeVi {

    @Schema(description = "sonarqube账号")
    private String account;

    @Schema(description = "sonarqube姓名")
    private String name;

    @Schema(description = "有问题类")
    private String component;
    
    @Schema(description = "异常说明")
    private String message;  //Make this method "public".
     
}
