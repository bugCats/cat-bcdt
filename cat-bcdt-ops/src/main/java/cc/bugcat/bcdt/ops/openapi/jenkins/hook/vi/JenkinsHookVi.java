package cc.bugcat.bcdt.ops.openapi.jenkins.hook.vi;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * */
@Data
@Schema
public class JenkinsHookVi {

    private String jobName;
    
    private String jobBaseName;
    
    private String buildUrl;
    
    private String gitBranch;

    private String buildUser;
    
    /**
     *
     *   "jobName": "dev-pms/pms-train",
     *   "jobBaseName": "pms-train",
     *   "buildUrl": "http://192.168.3.90:9003/job/dev-pms/job/pms-train/7/",
     *   "gitBranch": "origin/master"
     * 
     */

}
