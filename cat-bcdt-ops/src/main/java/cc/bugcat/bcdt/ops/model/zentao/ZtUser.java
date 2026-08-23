package cc.bugcat.bcdt.ops.model.zentao;

import cc.bugcat.bcdt.ops.model.ext.ZtUserExt;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * zt_user:get it
 * @author auto
 * 2018-11-29 11:02:26
 */
@Getter
@Setter
@TableName("zt_user")
public class ZtUser extends ZtUserExt {

    
    /**
     * git账号
     * */
    private String skype;
    
    
    

}