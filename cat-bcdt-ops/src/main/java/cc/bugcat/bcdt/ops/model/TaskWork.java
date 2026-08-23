package cc.bugcat.bcdt.ops.model;


import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author bugcat-cc
 */
@Data
public class TaskWork {
    
    private Long objectId; //数据id
    
    private String objectName; //名称
    
    private Integer objectType; //类型：10 task, 20 bug，30 需求，40 用例
    
    private String objectLink; //任务链接


    private String account;
    private Date startTime;
    private Date endTime;
    private Map<String, List<String>> whereSql;
    
}
