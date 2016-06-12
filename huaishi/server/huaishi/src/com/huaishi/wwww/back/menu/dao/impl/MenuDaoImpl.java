package com.huaishi.wwww.back.menu.dao.impl;

import org.springframework.stereotype.Repository;

import com.huaishi.wwww.back.menu.dao.MenuDao;
import com.huaishi.wwww.back.menu.entity.Menu;
import com.huaishi.wwww.base.dao.impl.BaseDaoImpl;

@Repository("menuDao")
public class MenuDaoImpl extends BaseDaoImpl<Menu> implements MenuDao {

	@Override
	public Class<Menu> getEntityClass() {
		return Menu.class;
	}

}
