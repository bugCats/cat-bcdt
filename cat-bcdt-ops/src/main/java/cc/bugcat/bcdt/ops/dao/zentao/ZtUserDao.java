package cc.bugcat.bcdt.ops.dao.zentao;

import cc.bugcat.acore.mybatis.plus.IBaseDao;
import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.bcdt.ops.model.zentao.ZtUser;
import org.springframework.stereotype.Repository;

@Repository
public interface ZtUserDao extends IBaseDao<ZtUser> {
    
    
    default ZtUser findByAccount(String account) {
        return selectOne(MyWrapper.of(ZtUser.class).eq(ZtUser::getAccount, account).limit(1));
    }
    
    
    default ZtUser findByRealname(String realname) {
        return selectOne(MyWrapper.of(ZtUser.class).eq(ZtUser::getRealname, realname).limit(1));
    }
}
