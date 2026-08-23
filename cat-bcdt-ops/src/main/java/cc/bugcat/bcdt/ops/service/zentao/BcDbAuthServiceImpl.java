package cc.bugcat.bcdt.ops.service.zentao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cc.bugcat.acore.mybatis.plus.BaseService;
import cc.bugcat.bcdt.ops.service.inter.BcDbAuthService;
import cc.bugcat.bcdt.ops.dao.zentao.BcDbAuthDao;
import cc.bugcat.bcdt.ops.model.zentao.BcDbAuth;

/**
 * 
 * BcDbAuthDao 已经被注入，在该类中，直接使用 dao.XX() 
 * @author auto
 * 2026-07-30 15:59:48
 * */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class BcDbAuthServiceImpl extends BaseService<BcDbAuthDao,BcDbAuth> implements BcDbAuthService{



}