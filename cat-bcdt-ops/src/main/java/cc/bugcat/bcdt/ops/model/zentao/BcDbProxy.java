package cc.bugcat.bcdt.ops.model.zentao;

import cc.bugcat.acore.mybatis.annotation.Encry;
import cc.bugcat.acore.mybatis.annotation.Encrys;
import cc.bugcat.bcdt.ops.model.ext.BcDbProxyExt;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * BcDbProxyExt 扩展类
 * bc_db_proxy:数据库代理配置
 * @author auto
 * 2026-08-10 13:15:48
 * */
@Getter
@Setter
@TableName("bc_db_proxy")
@Encrys
public class BcDbProxy extends BcDbProxyExt {

    @Encry
    @TableField("`dbauser`")
    private String dbaUser;	// dba账号

    @Encry
    @TableField("`dbapwd`")
    private String dbaPwd;	// dba密码

    @Encry
    @TableField("`writeuser`")
    private String writeUser;	// 写账号

    @Encry
    @TableField("`writepwd`")
    private String writePwd;	// 写密码

    @Encry
    @TableField("`readuser`")
    private String readUser;	// 只读账号

    @Encry
    @TableField("`readpwd`")
    private String readPwd;	// 只读密码

    @Encry
    @TableField("`whiteuser`")
    private String whiteUser;	// 白名单账号
    
}
