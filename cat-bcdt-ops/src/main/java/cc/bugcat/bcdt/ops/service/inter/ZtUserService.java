package cc.bugcat.bcdt.ops.service.inter;

import cc.bugcat.acore.mybatis.plus.IBaseService;
import cc.bugcat.bcdt.ops.model.zentao.ZtUser;

import java.util.Set;

/**
 * 
 * @author auto
 * 2026-07-30 16:08:19
 * */
public interface ZtUserService extends IBaseService<ZtUser> {

	/**
	 * 如果有批操作，请继承 cc.bugcat.common.framework.service.BaseBatchService
	 * */


	ZtUser findByAccount(String account);

	/**
	 * 通过第三方账号查询
	 * */
	ZtUser findGitByAccount(String realname, String account);

	/**
	 * 查询账号权限
	 * */
	Set<String> findOper(String account);
}
