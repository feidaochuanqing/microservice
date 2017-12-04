package com.founder.microservice.domain;

/**
 * 用户实体，获取静态的用户
 * @author: lizhch
 * @date: 2017年12月4日 下午4:53:51  
 * @version V1.0
 */
public class User {
	private Long id;
	private String name;

	public User() {}
	public User(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private static User user = new User(1L,"system");
	public static User getCurrentUser() {
		return user;
	}
}