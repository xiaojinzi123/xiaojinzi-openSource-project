package com.huaishi.wwww.back.menu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huaishi.wwww.back.menu.dao.MenuDao;
import com.huaishi.wwww.back.menu.dao.MenuItemDao;
import com.huaishi.wwww.back.menu.entity.Menu;
import com.huaishi.wwww.back.menu.entity.MenuItem;
import com.huaishi.wwww.back.menu.service.MenuItemService;
import com.huaishi.wwww.base.dao.BaseDao;
import com.huaishi.wwww.base.service.impl.BaseServiceImpl;
import com.huaishi.wwww.common.Result;

@Service("menuItemService")
public class MenuItemServiceImpl extends BaseServiceImpl<MenuItem> implements MenuItemService {

	@Autowired
	private MenuItemDao menuItemDao;

	@Autowired
	private MenuDao menuDao;

	@Override
	public BaseDao<MenuItem, Integer> getBaseDao() {
		return menuItemDao;
	}

	@Transactional
	@Override
	public List<MenuItem> queryByMenu(Menu menu) {
		return menuItemDao.queryByMenu(menu);
	}

	@Override
	protected boolean checkForInsert(MenuItem m, Result<MenuItem> result) {

		if (m.getName() == null || "".equals(m.getName())) { // 如果菜单的名字不为null,那么执行插入
			result.resultText = "the name must not be null or null string!";
			return false;
		}

		Integer menuId = m.getMenu().getId();

		if (menuId == null) {
			result.resultText = "the id of menu is null";
			return false;
		} else {
			Menu menu = menuDao.query(menuId);
			if (menu == null) {
				result.resultText = "the menu is not exist";
				return false;
			} else {
				m.setMenu(menu);
			}
		}

		return super.checkForInsert(m, result);
	}

}
