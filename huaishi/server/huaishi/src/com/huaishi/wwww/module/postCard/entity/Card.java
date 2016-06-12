package com.huaishi.wwww.module.postCard.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.huaishi.wwww.base.entity.BaseEntity;
import com.huaishi.wwww.module.regAndLogin.entity.User;

/**
 * 帖子的实体对象 litepal android使用的数据库框架
 * 
 * @author cxj
 *
 */
@Entity
@Table(name = "card")
public class Card extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* 主键id */
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 发帖的用户
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;

	/* 发帖的内容 */
	@Column
	private String content;

	/* 发帖的时间 */
	@Column
	private Date date;

	// =======================帖子的额外信息 satrt===========================

	/* 浏览次数,客户端点击查看详情算一次 */
	@Column
	private Integer views;

	/* 评论总数 */
	@Column
	private Integer comments;

	/* 点赞总数 */
	@Column
	private Integer popularity;

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
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

	@Override
	public String toString() {
		return "Card [id=" + id + ", user=" + user + ", content=" + content + ", date=" + date + ", views=" + views
				+ ", comments=" + comments + ", popularity=" + popularity + "]";
	}

}
