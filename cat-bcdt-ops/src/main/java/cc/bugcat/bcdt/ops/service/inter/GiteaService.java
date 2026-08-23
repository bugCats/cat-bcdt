package cc.bugcat.bcdt.ops.service.inter;


import cc.bugcat.bcdt.ops.moduls.gitea.GitData;

public interface GiteaService {

    
    void push(String reqStr);
    
    void pullRequest(String reqStr);

    void issueComment(String reqStr);

    void create(String reqStr);

    GitData queryFiles();

    GitData saveFiles(GitData vi);
}
