package com.springboot.ContactManager.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.springboot.ContactManager.dao.UserDao;
import com.springboot.ContactManager.entities.User;
import com.springboot.ContactManager.service.UserManagement;

@Controller
public class UserHandler {
	
	@Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserManagement userManagement;
	
	@Autowired
	private UserDao userDao;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Contact Manager");
		return "homepage";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Signup - Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute("user") User user, Model m) {
		m.addAttribute("user", user);
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userManagement.saveUserInfo(user);
		return "user_dashboard";
	}
	
	@GetMapping("/signin")
	public String login(Model m) {
		m.addAttribute("user", new User());	
		return "login";
	}
	
	@GetMapping("/user/dashboard")
	public String dashboard(Model m, Principal p) {
		String name = p.getName();
		User u = userDao.getUserByUserName(name);
		m.addAttribute("user",u);
		m.addAttribute("title", "Dashboard - Contact Manager");
		return "user_dashboard";
	}
	
}
