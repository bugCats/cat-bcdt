package cc.bugcat.bcdt.ops.service.zentao;

import cc.bugcat.acore.mybatis.plus.MyWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cc.bugcat.acore.mybatis.plus.BaseService;
import cc.bugcat.bcdt.ops.service.inter.BcDbProxyService;
import cc.bugcat.bcdt.ops.dao.zentao.BcDbProxyDao;
import cc.bugcat.bcdt.ops.model.zentao.BcDbProxy;

import java.util.List;

/**
 * 
 * BcDbProxyDao
 * @author auto
 * 2026-08-10 13:15:48
 * */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class BcDbProxyServiceImpl extends BaseService<BcDbProxyDao,BcDbProxy> implements BcDbProxyService{


    @Override
    public List<BcDbProxy> listByAble() {
        return baseMapper.selectList(MyWrapper.of(BcDbProxy.class).eq(BcDbProxy::getEnable, "1"));
    }
}