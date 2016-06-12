package com.huaishi.wwww.module.postCard.dao.impl;

import org.springframework.stereotype.Repository;

import com.huaishi.wwww.base.dao.impl.BaseDaoImpl;
import com.huaishi.wwww.module.postCard.dao.CardDao;
import com.huaishi.wwww.module.postCard.entity.Card;

@Repository("cardDao")
public class CardDaoImpl extends BaseDaoImpl<Card> implements CardDao {

	@Override
	public Class<Card> getEntityClass() {
		return Card.class;
	}

}
