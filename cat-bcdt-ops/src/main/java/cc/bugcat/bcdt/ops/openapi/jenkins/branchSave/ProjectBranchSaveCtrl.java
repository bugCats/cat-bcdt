package cc.bugcat.bcdt.ops.openapi.jenkins.branchSave;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.openapi.jenkins.branchSave.vi.ProjectBranchSaveVi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "jenkins 扩展" )
@RestController
public class ProjectBranchSaveCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "修改项目分支")
    @PostMapping("/jenkins/branch/save")
    @ACS(Opers.login)
    public ResultEntity<Void> openApi(@Validated @RequestBody List<ProjectBranchSaveVi> req) {
        return super.execute(ProjectBranchSaveService.class, req);
    }
    
}
