package cc.bugcat.bcdt.ops.moduls.gitea;

import lombok.Data;

import java.util.TreeMap;

/**
 * Gitea配置
 *
 * @author bugcat-cc
 */
@Data
public class GitData {

    /**
     * 关键文件
     * 仓库：文件
     * */
    public TreeMap<String, GitCheck> check;

    
}
