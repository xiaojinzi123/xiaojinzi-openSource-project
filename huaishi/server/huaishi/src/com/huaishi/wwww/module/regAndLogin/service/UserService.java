package com.huaishi.wwww.module.regAndLogin.service;

import com.huaishi.wwww.base.service.BaseService;
import com.huaishi.wwww.common.Result;
import com.huaishi.wwww.module.regAndLogin.entity.User;

/**
 * 用户的业务接口
 * 
 * @author cxj
 *
 */
public interface UserService extends BaseService<User, Integer> {

	/**
	 * 用户登陆业务
	 * 
	 * @param u
	 * @return
	 */
	public Result<User> login(User u);

	/**
	 * 注册业务
	 * 
	 * @param u
	 * @return
	 */
	public Result<User> register(User u);

	/**
	 * 是否存在
	 * 
	 * @param u
	 * @return
	 */
	public boolean isExist(User u);

}
