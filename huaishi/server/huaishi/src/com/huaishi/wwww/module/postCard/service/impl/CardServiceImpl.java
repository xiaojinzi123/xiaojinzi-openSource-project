package com.huaishi.wwww.module.postCard.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huaishi.wwww.base.dao.BaseDao;
import com.huaishi.wwww.base.service.impl.BaseServiceImpl;
import com.huaishi.wwww.common.Result;
import com.huaishi.wwww.module.postCard.dao.CardDao;
import com.huaishi.wwww.module.postCard.entity.Card;
import com.huaishi.wwww.module.postCard.service.CardService;
import com.huaishi.wwww.module.regAndLogin.dao.UserDao;
import com.huaishi.wwww.module.regAndLogin.entity.User;

@Service("cardService")
public class CardServiceImpl extends BaseServiceImpl<Card> implements CardService {

	@Autowired
	private CardDao cardDao;

	@Autowired
	private UserDao userDao;

	@Override
	public BaseDao<Card, Integer> getBaseDao() {
		return cardDao;
	}

	@Override
	protected boolean checkForInsert(Card entity, Result<Card> result) {

		Integer userId = entity.getUser().getId();

		if (userId == null) {
			result.resultText = "the id of user is null";
			return false;
		} else {
			User user = userDao.query(userId);
			if (user == null) {
				result.resultText = "the user is not exist";
				return false;
			} else {
				entity.setUser(user);
				// 初始化帖子有关的数据
				entity.setComments(0);
				entity.setPopularity(0);
				entity.setViews(0);
				entity.setDate(new Date(System.currentTimeMillis()));
			}
		}

		return super.checkForInsert(entity, result);
	}

}
