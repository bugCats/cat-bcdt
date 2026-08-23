/**
 * Copyright (C), 2017-2019, 北京壹嘉商旅
 *
 * @author: Administrator
 * @create: 2019/8/5 18:25
 * Desc:
 */
package cc.bugcat.jinkens.plugin.vi;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitBranchSaveVi {

    
    private String fpath;       //文件路径

    private String branch;      //仓库名

    private String editUser;    //修改人

    
    public String getFpath() {
        return fpath;
    }
    public void setFpath(String fpath) {
        this.fpath = fpath;
    }

    public String getBranch() {
        return branch;
    }
    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getEditUser() {
        return editUser;
    }
    public void setEditUser(String editUser) {
        this.editUser = editUser;
    }


}
