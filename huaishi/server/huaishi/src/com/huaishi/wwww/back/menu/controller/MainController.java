package com.huaishi.wwww.back.menu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.huaishi.wwww.back.menu.entity.Menu;
import com.huaishi.wwww.back.menu.entity.MenuItem;
import com.huaishi.wwww.back.menu.service.MenuItemService;
import com.huaishi.wwww.back.menu.service.MenuService;

/**
 * 初始化后台的界面
 * 
 * @dateTime 2016年1月5日 下午5:19:43
 * @Company xjzCompany
 * @author xiaojinzi
 */
@RequestMapping("/back/main")
@Controller
public class MainController {

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuItemService menuItemService;

	/**
	 * 初始化后台的左边部分
	 * 
	 * @return
	 */
	@RequestMapping("initLeft")
	public ModelAndView initLeft() {

		ModelAndView mv = new ModelAndView();

		// 查询处所有的菜单
		List<Menu> menus = menuService.queryAll();

		for (int i = 0; i < menus.size(); i++) {
			// 循环中的菜单
			Menu menu = menus.get(i);
			// 菜单中的菜单条目
			List<MenuItem> menuItems = menuItemService.queryByMenu(menu);
			menu.setMenuItems(menuItems);
		}

		mv.setViewName("back/initLayout/left");
		mv.addObject("menus", menus);
		
		return mv;
	}

	/**
	 * 初始化后台的中间的部分
	 * 
	 * @return
	 */
	@RequestMapping("initCenter")
	public String initCenter() {
		return "back/initLayout/center";
	}

	/**
	 * 初始化后台的顶端部分
	 * 
	 * @return
	 */
	@RequestMapping("initTop")
	public String initTop() {
		return "back/initLayout/top";
	}

	/**
	 * 初始化后台的界面
	 * 
	 * @return
	 */
	@RequestMapping("initMain")
	public String initMain() {
		return "back/initLayout/main";
	}

}
