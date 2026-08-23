/**
 * Copyright (C), 2017-2019, 北京壹嘉商旅
 *
 * @author: Administrator
 * @create: 2019/8/6 11:48
 * Desc:
 */
package cc.bugcat.jinkens.plugin.vo;



import java.util.List;

public class GirProjectListVo {

    private String projectId;

    private String projectName; //仓库名

    private List<BranchInfo> branchInfos;


    public static class BranchInfo {

        private String branchId;    //分支id

        private String branchName;  //分支名称

        private String protecteds;  //是否为受保护的分支
        
        
        public String getBranchId() {
            return branchId;
        }
        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getBranchName() {
            return branchName;
        }
        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getProtecteds() {
            return protecteds;
        }
        public void setProtecteds(String protecteds) {
            this.protecteds = protecteds;
        }
    }


    
    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<BranchInfo> getBranchInfos() {
        return branchInfos;
    }
    public void setBranchInfos(List<BranchInfo> branchInfos) {
        this.branchInfos = branchInfos;
    }
}
