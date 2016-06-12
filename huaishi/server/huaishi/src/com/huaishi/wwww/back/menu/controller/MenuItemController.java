package com.huaishi.wwww.back.menu.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huaishi.wwww.back.menu.entity.MenuItem;
import com.huaishi.wwww.back.menu.service.MenuItemService;
import com.huaishi.wwww.back.menu.service.MenuService;
import com.huaishi.wwww.base.controller.base.BaseController;
import com.huaishi.wwww.base.service.BaseService;

import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

@Controller
@RequestMapping("back/menuItem")
public class MenuItemController extends BaseController<MenuItem> {

	private JsonConfig cfg = new JsonConfig();

	public MenuItemController() {
		super();
		cfg.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object source, String name, Object value) {
				if ("menu".equals(name)) {
					return true;
				}
				return false;
			}
		});
	}
	
	@Override
	protected JsonConfig getJsonConfig() {
		return cfg;
	}

	@Autowired
	private MenuItemService menuItemService;

	@Autowired
	private MenuService menuService;

	@Override
	protected BaseService<MenuItem, Integer> getBaseService() {
		return menuItemService;
	}
	
	@Override
	protected String getListUrl() {
		return "back/menu/list/menuItemList";
	}

	@Override
	protected void goToListPager(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 转发到查询的页面
		request.getRequestDispatcher("/menuItem/list").forward(request, response);
	}

	

}
