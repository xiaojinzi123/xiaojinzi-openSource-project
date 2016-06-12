package com.huaishi.wwww.back.menu.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "m_menu")
public class Menu {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String name; // 菜单的名字

	@JoinColumn(name = "menu_id")
	@OneToMany(cascade = CascadeType.ALL, targetEntity = MenuItem.class, fetch = FetchType.EAGER)
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();

	public Menu() {
		super();
	}

	public Menu(Integer id) {
		super();
		this.id = id;
	}

	public Menu(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
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

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

}
