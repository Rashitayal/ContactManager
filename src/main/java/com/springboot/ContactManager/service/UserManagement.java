package com.springboot.ContactManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.ContactManager.dao.UserDao;
import com.springboot.ContactManager.entities.User;

@Service
public class UserManagement {
	
	@Autowired
	private UserDao userDao;
	
	public User saveUserInfo(User u) {
		userDao.save(u);
		return u;
	}

}
