package com.springboot.ContactManager.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.springboot.ContactManager.dao.UserDao;
import com.springboot.ContactManager.entities.User;
import com.springboot.ContactManager.service.ContactManagement;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
	private ContactManagement contactManagement;
	
	@Autowired
	private UserDao userDao;

	@GetMapping("/contacts/{keyword}")
	public ResponseEntity<?> findContactByKeyword(@PathVariable("keyword") String Keyword, Principal p){
		System.out.println("dsadada");
		User user = userDao.getUserByUserName(p.getName());
		return ResponseEntity.ok(contactManagement.findContactByKeyword(Keyword, user.getUserId()));
	}
}
