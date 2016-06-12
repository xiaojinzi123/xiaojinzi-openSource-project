package com.huaishi.wwww.back.menu.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.huaishi.wwww.back.menu.dao.MenuItemDao;
import com.huaishi.wwww.back.menu.entity.Menu;
import com.huaishi.wwww.back.menu.entity.MenuItem;
import com.huaishi.wwww.base.dao.impl.BaseDaoImpl;

@Repository("menuItemDao")
public class MenuItemDaoImpl extends BaseDaoImpl<MenuItem> implements MenuItemDao {

	@Override
	public Class<MenuItem> getEntityClass() {
		return MenuItem.class;
	}

	@Override
	public List<MenuItem> queryByMenu(Menu menu) {
		String hql = "FROM MenuItem m where m.menu = ?";
		return queryForPagerList(hql, menu);
	}

}
