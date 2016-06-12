package com.huaishi.wwww.back.menu.dao;

import java.util.List;

import com.huaishi.wwww.back.menu.entity.Menu;
import com.huaishi.wwww.back.menu.entity.MenuItem;
import com.huaishi.wwww.base.dao.BaseDao;

public interface MenuItemDao extends BaseDao<MenuItem, Integer> {

	/**
	 * 根据菜单获取这个菜单的所有菜单条目
	 * 
	 * @param menu
	 *            菜单对象
	 * @return
	 */
	public List<MenuItem> queryByMenu(Menu menu);

}
