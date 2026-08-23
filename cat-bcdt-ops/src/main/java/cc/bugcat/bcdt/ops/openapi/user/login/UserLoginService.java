package cc.bugcat.bcdt.ops.openapi.user.login;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.acore.framework.utils.MdEncrypt;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.acore.webmvc.utils.ServletUtil;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.model.zentao.ZtUser;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfoVo;
import cc.bugcat.bcdt.ops.openapi.user.login.vi.UserLoginVi;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.service.inter.ZtUserService;
import cc.bugcat.bcdt.ops.utils.PasswordCheckUtil;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class UserLoginService extends AbstractOpenApiService<UserLoginVi, UserInfoVo> {
    
    
    @Autowired
    private ZtUserService ztUserService;
    
    
    
    @Override
    @Transactional(readOnly = true)
    public UserInfoVo execute(UserLoginVi reqVi, RequestContext context) {
        
        //密码强度校验
        PasswordCheckUtil.checkPassword(reqVi.getPassword());
        
        String ipAddr = ServletUtil.getIpAddr(context.getRequest());
        
        ZtUser ztUser = ztUserService.findByAccount(reqVi.getUsername());
        if( ztUser == null ){
            throw BusinessException.builder("500", Errors.SYS_ERR, "账号或密码错误！");
        }
        
        String pwd = MdEncrypt.md5(reqVi.getPassword());
        if( !pwd.equals(ztUser.getPassword()) ){
            throw BusinessException.builder("500", Errors.SYS_ERR, "账号或密码错误！");
        }
        
        Set<String> opers = new HashSet<>();
        opers.add(Opers.login);
        Set<String> oper = ztUserService.findOper(ztUser.getAccount());
        opers.addAll(oper);
        
        UserInfo info = new UserInfo();
        info.setToken(MdEncrypt.md5(ObjectUtil.uuid()));
        info.setAccount(ztUser.getAccount());
        info.setRealname(ztUser.getRealname());
        info.setPhone(ztUser.getPhone());
        info.setIpAddr(ipAddr);
        info.setLoginTime(DateUtil.ll.getDate());
        info.setOpers(opers);
        
        UserUtil.setUserInfo(info);
        
        return BeanCopyUtil.copy(info, UserInfoVo::new);
    }
    
    
    
}
