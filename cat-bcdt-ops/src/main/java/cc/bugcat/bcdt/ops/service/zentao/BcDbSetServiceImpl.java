package cc.bugcat.bcdt.ops.service.zentao;

import cc.bugcat.acore.mybatis.plus.MyWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cc.bugcat.acore.mybatis.plus.BaseService;
import cc.bugcat.bcdt.ops.service.inter.BcDbSetService;
import cc.bugcat.bcdt.ops.dao.zentao.BcDbSetDao;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 * BcDbSetDao 已经被注入，在该类中，直接使用 dao.XX() 
 * @author auto
 * 2026-07-31 14:31:59
 * */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class BcDbSetServiceImpl extends BaseService<BcDbSetDao,BcDbSet> implements BcDbSetService{


    @Override
    public BcDbSet getByCode(String databaseCode) {
        return baseMapper.selectOne(MyWrapper.of(BcDbSet.class).eq(BcDbSet::getCode, databaseCode));
    }


    @Override
    public Map<Long, BcDbSet> getDbSetMap() {
        List<BcDbSet> list = baseMapper.selectList(MyWrapper.of(BcDbSet.class).select(BcDbSet::getId, BcDbSet::getCode, BcDbSet::getDesc));
        return Optional.ofNullable(list).orElseGet(Collections::emptyList).stream().collect(Collectors.toMap(BcDbSet::getId, Function.identity()));
    }


    @Override
    public BcDbSet selectByName(String name) {
        return baseMapper.selectByName(name);
    }

    @Override
    public List<String> selectByBean(String name) {
        BcDbSet query = new  BcDbSet();
        query.setUsername(name);
        return baseMapper.selectByBean(query);
    }
}