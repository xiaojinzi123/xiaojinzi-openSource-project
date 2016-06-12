package com.huaishi.wwww.back.menu.entity;

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

@Entity
@Table(name = "m_menuItem")
public class MenuItem {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String name; // 菜单条目的名字

	@Column
	private String url; // 菜单条目的链接

	// PERSIST: 存留
	// MERGE: 合并 混合
	@ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER, targetEntity = Menu.class)
	@JoinColumn(name = "menu_id")
	private Menu menu;

	public MenuItem() {
		super();
	}

	public MenuItem(Integer id) {
		super();
		this.id = id;
	}

	public MenuItem(Integer id, String name, String url) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@Override
	public String toString() {
		return "MenuItem [id=" + id + ", name=" + name + ", url=" + url + "]";
	}
}
