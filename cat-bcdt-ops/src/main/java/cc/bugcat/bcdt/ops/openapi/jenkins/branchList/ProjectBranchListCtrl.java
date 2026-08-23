package cc.bugcat.bcdt.ops.openapi.jenkins.branchList;

import cc.bugcat.acore.framework.annotation.ACS;
import cc.bugcat.acore.framework.entity.ResultEntity;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiCtrl;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.openapi.jenkins.branchList.vo.ProjectBranchListVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 *
 * @author bugcat-cc
 */
@Tag(name = "jenkins 扩展" )
@RestController
public class ProjectBranchListCtrl extends AbstractOpenApiCtrl {
    
    @Operation(summary = "Jenkins项目分支列表")
    @GetMapping("/jenkins/branch/list")
    @ACS(Opers.login)
    public ResultEntity<List<ProjectBranchListVo>> openApi() {
        return super.execute(ProjectBranchListService.class, null);
    }
    
}
