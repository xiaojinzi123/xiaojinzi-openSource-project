package com.huaishi.wwww.module.regAndLogin.dao.impl;

import org.springframework.stereotype.Repository;

import com.huaishi.wwww.base.dao.impl.BaseDaoImpl;
import com.huaishi.wwww.module.regAndLogin.dao.UserDao;
import com.huaishi.wwww.module.regAndLogin.entity.User;

/**
 * 用户的数据库操作对象
 * 
 * @author cxj
 *
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}

}
