package cc.bugcat.bcdt.ops.service.inter;


import cc.bugcat.acore.mybatis.plus.IBaseService;
import cc.bugcat.bcdt.ops.model.zentao.BcParam;

/**
 *
 * @author auto
 * 2024-04-10 13:59:04
 * */
public interface BcParamService extends IBaseService<BcParam> {


    String getValue(String paramId);
    
    String getValue(String paramId, String defaultValue);

    
}