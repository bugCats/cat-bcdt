package cc.bugcat.bcdt.ops.service.inter;

import cc.bugcat.acore.mybatis.plus.IBaseService;
import cc.bugcat.bcdt.ops.model.zentao.BcDbProxy;

import java.util.List;

/**
 * 
 * @author auto
 * 2026-08-10 13:15:48
 * */
public interface BcDbProxyService extends IBaseService<BcDbProxy> {


    List<BcDbProxy> listByAble();
    
    
}
