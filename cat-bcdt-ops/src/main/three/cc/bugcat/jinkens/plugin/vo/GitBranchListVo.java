/**
 * Copyright (C), 2017-2019, 北京壹嘉商旅
 *
 * @author: Administrator
 * @create: 2019/8/2 17:30
 * Desc:
 */
package cc.bugcat.jinkens.plugin.vo;



import java.util.List;
import java.util.Map;


public class GitBranchListVo {


    private String groupName;

    private Map<String, List<Depository>> dictDeps;


    public static class Depository {

        private String projectName;

        private String branchName;

        private String configPath;
        
        private String editUser;

        public String getProjectName() {
            return projectName;
        }
        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getBranchName() {
            return branchName;
        }
        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getConfigPath() {
            return configPath;
        }
        public void setConfigPath(String configPath) {
            this.configPath = configPath;
        }

        public String getEditUser() {
            return editUser;
        }
        public void setEditUser(String editUser) {
            this.editUser = editUser;
        }
    }


    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Map<String, List<Depository>> getDictDeps() {
        return dictDeps;
    }
    public void setDictDeps(Map<String, List<Depository>> dictDeps) {
        this.dictDeps = dictDeps;
    }
}
