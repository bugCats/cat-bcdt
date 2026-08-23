package cc.bugcat.bcdt.ops.service.inter;


import cc.bugcat.bcdt.ops.openapi.gitlab.vi.GitlabHooksPullVi;

public interface GitlabService {

    
    void pullRequest(GitlabHooksPullVi vi);
    
}
