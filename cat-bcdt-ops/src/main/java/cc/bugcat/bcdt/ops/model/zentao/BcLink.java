package cc.bugcat.bcdt.ops.model.zentao;

import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.bcdt.ops.model.ext.BcLinkExt;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * BcLinkExt 扩展类
 * bc_link:超链接按钮
 * @author auto
 * 2026-07-30 16:04:36
 * */
@Getter
@Setter
@TableName("bc_link")
public class BcLink extends BcLinkExt {


    private String colId;	// 所属列

    private String labelId;	// 所属子类

    private JsonTools htmlExtra;
    
}
