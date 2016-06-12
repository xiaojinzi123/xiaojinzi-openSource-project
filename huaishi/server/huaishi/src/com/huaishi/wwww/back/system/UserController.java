package com.huaishi.wwww.back.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huaishi.wwww.base.controller.base.BaseController;
import com.huaishi.wwww.base.service.BaseService;
import com.huaishi.wwww.module.regAndLogin.entity.User;
import com.huaishi.wwww.module.regAndLogin.service.UserService;

@Controller
@RequestMapping("back/user")
public class UserController extends BaseController<User> {
	
	@Autowired
	private UserService userService;

	@Override
	protected BaseService<User, Integer> getBaseService() {
		return userService;
	}

}
