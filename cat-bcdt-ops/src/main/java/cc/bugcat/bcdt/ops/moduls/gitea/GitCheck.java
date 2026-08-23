package cc.bugcat.bcdt.ops.moduls.gitea;

import lombok.Data;

import java.util.TreeSet;

/**
 * 关键文件监听
 *
 * @author bugcat-cc
 */
@Data
public class GitCheck {

    /**
     * 仓库名
     * */
    private String repoName;
    
    /**
     * 名称名称：cc.bucat.xxxx
     * */
    private TreeSet<String> fileName;


}
