package com.huaishi.wwww.client.postCard.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huaishi.wwww.base.controller.base.BaseController;
import com.huaishi.wwww.base.service.BaseService;
import com.huaishi.wwww.module.postCard.entity.Card;
import com.huaishi.wwww.module.postCard.service.CardService;

@Controller
@RequestMapping("client/card")
public class CardController extends BaseController<Card> {

	@Autowired
	private CardService cardService;

	@Override
	protected BaseService<Card, Integer> getBaseService() {
		return cardService;
	}

	@RequestMapping("insertCard")
	public void insertCard(Card c, Boolean isWriteObject, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		insert(c, true, true, request, response);

	}

}
