package com.example.cxj.huaishi.modular.postCard.entity;

import com.example.cxj.huaishi.common.entity.User;

/**
 * 帖子的实体对象
 * litepal android使用的数据库框架
 * 
 * @author cxj
 *
 */

public class Card {

	private Integer id;

	private User user;

	/* 发帖的内容 */
	private String content;

	/* 发帖的时间 */
	private long date;

	// =======================帖子的额外信息 satrt===========================

	/* 浏览次数,客户端点击查看详情算一次 */
	private Integer views;

	/* 评论总数 */
	private Integer comments;

	/* 点赞总数 */
	private Integer popularity;

	/**
	 * 这个标识发帖时候的图片地址,用标识符";"隔开,一个变量表示多个地址
	 */
	private String images;

	// =======================帖子的额外信息 end=============================

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Integer getComments() {
		return comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}

	public Integer getPopularity() {
		return popularity;
	}

	public void setPopularity(Integer popularity) {
		this.popularity = popularity;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", user=" + user + ", content=" + content + ", date=" + date + ", views=" + views
				+ ", comments=" + comments + ", popularity=" + popularity + ", images=" + images + "]";
	}

}
