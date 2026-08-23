package cc.bugcat.bcdt.ops.model.zentao;

import cc.bugcat.acore.mybatis.annotation.Encry;
import cc.bugcat.acore.mybatis.annotation.Encrys;
import cc.bugcat.bcdt.ops.model.ext.BcDbSetExt;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * BcDbSetExt 扩展类
 * bc_db_set:数据库配置
 * @author auto
 * 2026-07-31 14:31:59
 * */
@Getter
@Setter
@Encrys
@TableName("bc_db_set")
public class BcDbSet extends BcDbSetExt {

    @Encry
    @TableField("`username`")
    private String username;	// 数据库账号

    @Encry
    @TableField("`password`")
    private String password;	// 数据库密码
    
}
