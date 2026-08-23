package cc.bugcat.bcdt.ops.service.zentao;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cc.bugcat.acore.mybatis.plus.BaseService;
import cc.bugcat.bcdt.ops.service.inter.BcLinkService;
import cc.bugcat.bcdt.ops.dao.zentao.BcLinkDao;
import cc.bugcat.bcdt.ops.model.zentao.BcLink;

/**
 * 
 * BcLinkDao 已经被注入，在该类中，直接使用 dao.XX() 
 * @author auto
 * 2026-07-30 16:04:36
 * */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class BcLinkServiceImpl extends BaseService<BcLinkDao,BcLink> implements BcLinkService{



}