package com.huaishi.wwww.module.regAndLogin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huaishi.wwww.base.dao.BaseDao;
import com.huaishi.wwww.base.service.impl.BaseServiceImpl;
import com.huaishi.wwww.common.Result;
import com.huaishi.wwww.module.regAndLogin.dao.UserDao;
import com.huaishi.wwww.module.regAndLogin.entity.User;
import com.huaishi.wwww.module.regAndLogin.service.UserService;

/**
 * 用户的业务对象
 * 
 * @author cxj
 *
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public BaseDao<User, Integer> getBaseDao() {
		return userDao;
	}

	@Transactional
	@Override
	public Result<User> login(User u) {

		Result<User> result = new Result<User>();

		// 先验证必须的密码
		if (u.getPassword() == null || "".equals(u.getPassword())) {
			result.resultText = "password is empty";
			return result;
		}

		// 先检验电话号码
		if (u.getPhoneNumber() != null && !"".equals(u.getPhoneNumber())) {

			// 根据手机号和密码查询
			String hql = "FROM User u where u.phoneNumber = ? and u.password = ?";
			List<User> users = userDao.queryForPagerList(hql, u.getPhoneNumber(), u.getPassword());

			if (users != null && users.size() > 0) {
				result.resultText = "login success";
				result.entity = users.get(0);
				return result;
			} else {
				result.resultText = "user is not exist";
			}
		}

		if (u.getName() != null && !"".equals(u.getName())) {

			// 根据用户名和密码查询
			String hql = "FROM User u where u.name = ? and u.password = ?";
			List<User> users = userDao.queryForPagerList(hql, u.getName(), u.getPassword());

			if (users == null || users.size() == 0) {
				result.resultText = "the user is not exist";
				return result;
			}

			result.resultText = "login success";
			result.entity = users.get(0);
			return result;

		} else {
			if (result.resultText == null) {
				result.resultText = "one of the name and phoneNumber must not be empty";
			}
			return result;
		}

	}

	@Override
	protected boolean checkForInsert(User u, Result<User> result) {

		if (u.getPassword() == null || "".equals(u.getPassword())) {
			result.resultText = "password is empty";
			return false;
		}

		if (u.getName() != null && !"".equals(u.getName())) {

			// 判断这个用户是不是存在
			boolean b = isExist(u);

			if (b) {
				result.resultText = "the name is exist";
				return false;
			}

		} else if (u.getPhoneNumber() != null && !"".equals(u.getPhoneNumber())) {

			// 判断这个用户是不是存在
			boolean b = isExist(u);

			if (b) {
				result.resultText = "the name is exist";
				return false;
			}

		} else {
			result.resultText = "one of the name and phoneNumber must not be empty";
			return false;
		}

		// 先写上失败,成功之后会被修改
		result.resultText = "register fail";

		return super.checkForInsert(u, result);
	}

	@Override
	protected void afterInsert(User u, Result<User> result) {
		super.afterInsert(u, result);
		result.resultText = "register success";
	}

	@Transactional
	@Override
	public Result<User> register(User u) {

		Result<User> result = new Result<User>();

		boolean b = save(u, result);

		if (!b) {
			result.entity = null;
		}

		return result;
	}

	@Transactional
	@Override
	public boolean isExist(User u) {

		String hql = null;

		if (u.getName() != null && !"".endsWith(u.getName())) {
			hql = "FROM User u where u.name = ?";
			List<User> users = userDao.queryForPagerList(hql, u.getName());
			if (users != null && users.size() != 0) {
				return true;
			}
		} else if (u.getPhoneNumber() != null && !"".endsWith(u.getPhoneNumber())) {
			hql = "FROM User u where u.phoneNumber = ?";
			List<User> users = userDao.queryForPagerList(hql, u.getPhoneNumber());
			if (users != null && users.size() != 0) {
				return true;
			}
		}

		return false;
	}

}
