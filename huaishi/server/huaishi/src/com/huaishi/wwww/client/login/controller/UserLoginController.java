package com.huaishi.wwww.client.login.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huaishi.wwww.common.Msg;
import com.huaishi.wwww.common.Result;
import com.huaishi.wwww.module.regAndLogin.entity.User;
import com.huaishi.wwww.module.regAndLogin.service.UserService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("client/user")
public class UserLoginController {

	@Autowired
	private UserService userService;

	@RequestMapping("login")
	public void login(HttpServletRequest request, HttpServletResponse response, User u) throws IOException {

		Result<User> result = userService.login(u);
		User user = result.entity;

		Msg m = new Msg();

		if (user != null) {
			m.setMsgText(result.resultText);
			m.setData(user);
		} else {
			m.setMsg(Msg.ERROR);
			m.setMsgText(result.resultText);
		}

		JSONObject jsonObject = JSONObject.fromObject(m);

		response.getOutputStream().print(jsonObject.toString());

	}

}
