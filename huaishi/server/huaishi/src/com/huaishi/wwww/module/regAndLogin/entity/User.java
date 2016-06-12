package com.huaishi.wwww.module.regAndLogin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.huaishi.wwww.base.entity.BaseEntity;

@Entity
@Table(name = "user")
public class User extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* 主键id */
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/* 用户名,唯一 */
	@Column(name = "name", unique = true)
	private String name;

	/* 密码 */
	@Column
	private String password;

	/* 电话号码 */
	@Column(name = "phoneNumber", unique = true)
	private String phoneNumber;

	/* 头像地址,有默认地址 */
	@Column(name = "avatarAddress")
	private String avatarAddress;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", phoneNumber=" + phoneNumber
				+ ", avatarAddress=" + avatarAddress + "]";
	}

}
