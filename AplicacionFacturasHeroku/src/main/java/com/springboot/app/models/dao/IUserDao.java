package com.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.app.models.entity.User;

public interface IUserDao extends CrudRepository<User, Long> {
	
	public User findByUsername(String user);
}
