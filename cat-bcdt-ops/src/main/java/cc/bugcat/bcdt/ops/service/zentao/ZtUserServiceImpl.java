package cc.bugcat.bcdt.ops.service.zentao;

import cc.bugcat.acore.mybatis.plus.BaseService;
import cc.bugcat.bcdt.ops.dao.zentao.ZtUserDao;
import cc.bugcat.bcdt.ops.dao.zentao.ZtUsergroupDao;
import cc.bugcat.bcdt.ops.model.zentao.ZtUser;
import cc.bugcat.bcdt.ops.service.inter.ZtUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author auto
 * 2018-11-29 09:56:20
 */
@Service
public class ZtUserServiceImpl extends BaseService<ZtUserDao, ZtUser> implements ZtUserService {
    
    @Autowired
    private ZtUsergroupDao usergroupDao;
    
    
    @Override
    public ZtUser findByAccount (String account) {
        ZtUser user = baseMapper.findByAccount(account);
        return user;
    }
    
    
    @Override
    public ZtUser findGitByAccount(String realname, String account) {
        ZtUser user = baseMapper.findByAccount(account);
        if( user == null ){
            if( StringUtils.isNotBlank(realname) ){
                user = baseMapper.findByRealname(realname);
            }
        }
        return user;
    }
    
    
    /**
     * 查询禅道zt_usergroup
     * */
    @Override
    public Set<String> findOper(String account){
        if( StringUtils.isBlank(account) ){
            return new HashSet<>();
        }
        List<String> groups = usergroupDao.findOperByAccount(account);
        return Optional.ofNullable(groups)
                .orElseGet(ArrayList::new)
                .stream()
                .map(desc -> desc.split("@")[0])
                .collect(Collectors.toSet());
    }

}
