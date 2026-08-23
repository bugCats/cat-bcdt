package cc.bugcat.bcdt.ops.service.zentao;

import cc.bugcat.acore.mybatis.plus.IBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cc.bugcat.acore.mybatis.plus.BaseService;
import cc.bugcat.bcdt.ops.dao.zentao.ZtBugDao;
import cc.bugcat.bcdt.ops.model.zentao.ZtBug;

/**
 * 
 * ZtBugDao 已经被注入，在该类中，直接使用 dao.XX() 
 * @author auto
 * 2026-07-30 16:06:30
 * */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class ZtBugServiceImpl extends BaseService<ZtBugDao,ZtBug> implements IBaseService<ZtBug> {



}