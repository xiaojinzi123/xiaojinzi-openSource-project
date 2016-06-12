package com.huaishi.wwww.back.menu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huaishi.wwww.back.menu.dao.MenuDao;
import com.huaishi.wwww.back.menu.entity.Menu;
import com.huaishi.wwww.back.menu.service.MenuService;
import com.huaishi.wwww.base.dao.BaseDao;
import com.huaishi.wwww.base.service.impl.BaseServiceImpl;
import com.huaishi.wwww.common.Result;

@Service("menuService")
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {

	@Autowired
	private MenuDao menuDao;

	@Override
	public BaseDao<Menu, Integer> getBaseDao() {
		return menuDao;
	}

	@Override
	protected boolean checkForInsert(Menu e, Result<Menu> result) {

		boolean b = e.getName() != null && !"".equals(e.getName());
		if (!b) {
			result.resultText = "the name must not be null or null String";
			return false;
		}

		return super.checkForInsert(e, result);
	}

	@Override
	protected String doUpdate(Menu entity, Menu readEntity) {

		// 更新实体
		readEntity.setName(entity.getName());

		return super.doUpdate(entity, readEntity);
	}

}
