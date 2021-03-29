package com.springboot.ContactManager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.springboot.ContactManager.dao.UserDao;
import com.springboot.ContactManager.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.getUserByUserName(username); 
		if(user==null) { 
			throw new UsernameNotFoundException("User Not Found"); 
			}
		return new UserDetailsImpl(user);
	}

}
