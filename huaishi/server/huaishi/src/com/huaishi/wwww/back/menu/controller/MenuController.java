package com.huaishi.wwww.back.menu.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huaishi.wwww.back.menu.entity.Menu;
import com.huaishi.wwww.back.menu.service.MenuService;
import com.huaishi.wwww.base.controller.base.BaseController;
import com.huaishi.wwww.base.service.BaseService;
import com.huaishi.wwww.common.Msg;

@Controller
@RequestMapping("back/menu")
public class MenuController extends BaseController<Menu> {

	@Autowired
	private MenuService menuService;

	/**
	 * 获取所有菜单的json数据
	 * 
	 * @throws IOException
	 */
	@RequestMapping("getAllJsonMenu")
	public void getAllJsonMenu(HttpServletResponse response) throws IOException {
		List<Menu> menus = menuService.queryAll();
		for (int i = 0; i < menus.size(); i++) {
			Menu menu = menus.get(i);
			menu.setMenuItems(null);
		}
		setMsgInfo(true, Msg.OK, menus, null, response);
	}

	/**
	 * 转发到查询的页面
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void goToListPager(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 转发到查询的页面
		request.getRequestDispatcher("/back/menu/list").forward(request, response);
	}

	@Override
	protected BaseService<Menu, Integer> getBaseService() {
		return menuService;
	}

	@Override
	protected String getListUrl() {
		return "back/menu/list/menuList";
	}

}
