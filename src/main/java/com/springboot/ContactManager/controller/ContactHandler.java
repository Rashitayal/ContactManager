package com.springboot.ContactManager.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.ContactManager.dao.UserDao;
import com.springboot.ContactManager.entities.Contact;
import com.springboot.ContactManager.entities.User;
import com.springboot.ContactManager.service.ContactManagement;
import com.springboot.ContactManager.service.UserManagement;

@Controller
public class ContactHandler {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ContactManagement contactManagement;
	
	@Autowired
	private UserManagement userManagement;
	
	/*
	 * @Autowired private ImageManager imageManager;
	 */
	
	@GetMapping("/user/addContact")
	public String addContact(Model m, Principal p) {
		String name = p.getName();
		User u = userDao.getUserByUserName(name);
		m.addAttribute("user",u);
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact",new Contact());
		return "add_contact";
	}
	
	//@RequestMapping(value=("/user/process/addContact"),headers=("content-type=multipart/*"),method=RequestMethod.POST)
	@PostMapping("/user/process/addContact")	
	public String processAddContact(@ModelAttribute ("contact") Contact contact,@RequestParam("image") MultipartFile image, Principal p, HttpSession httpSession, Model m) {
		String name = p.getName();
		User u = userDao.getUserByUserName(name);
			if(!StringUtils.hasText(image.getOriginalFilename())) {
				contact.setContactImage("contact.jpg");
			} else {
				contact.setContactImage(image.getOriginalFilename());
				
				try {
					File file = new ClassPathResource("static/img").getFile();
					Path path = Paths.get(file.getAbsolutePath()+File.separator+image.getOriginalFilename());
					Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			u.getContacts().add(contact);
			contact.setUser(u);
			httpSession.setAttribute("message", "contact added successfully");
		
		userManagement.saveUserInfo(u);
		
		return addContact(m, p);
	}
	
	//per page 5
	//current page
	
	@GetMapping("/user/contacts/{page}")
	public String getContactsView(@PathVariable("page") int page, Model m, Principal p) {
		String name = p.getName();
		User u = userDao.getUserByUserName(name);
		m.addAttribute("user",u);
		m.addAttribute("title", "All Contacts");
		PageRequest pageRequest =PageRequest.of(page, 5);
		Page<Contact> paginatedContacts =contactManagement.getAllContacts(u.getUserId(),pageRequest);
		m.addAttribute("contactList",paginatedContacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", paginatedContacts.getTotalPages());
		return "view_contacts";
	}
	
	@GetMapping("/user/viewContact/{contactId}")
	public String getContactById(@PathVariable("contactId") int contactId, Model m, Principal p) {
		String name = p.getName();
		User u = userDao.getUserByUserName(name);
		m.addAttribute("user",u);
		Contact derivedContact = contactManagement.getContactById(contactId);
		if(derivedContact.getUser().getUserId() == u.getUserId()) {
			m.addAttribute("contactInfo",derivedContact);
			return "view_contactbyId";
		}
		return "error";
	}
	
	@GetMapping("/user/delete/contact/{contactId}")
	public String deleteContactById(@PathVariable("contactId") int contactId, Model m, Principal p) {
		System.out.println("i am here inside");
		Contact derivedContact = contactManagement.getContactById(contactId);
		derivedContact.setUser(null);
		contactManagement.deleteContactById(derivedContact);	
		return "redirect:/user/contacts/0";
	}
	
	@GetMapping("/user/contact/update/{contactId}")
	public String getContactToUpdateById(@PathVariable("contactId") int contactId, Model  m, Principal p) {
		String name = p.getName();
		User u = userDao.getUserByUserName(name);
		m.addAttribute("user",u);
		m.addAttribute("title", "Update Contact");
		m.addAttribute("contact",contactManagement.getContactById(contactId));
		return "update_contact";
	}
	
	@PostMapping("/user/process/updateContact")
	public String processContactUpdate(@ModelAttribute ("contact") Contact contact,@RequestParam("image") MultipartFile image, Principal p, HttpSession httpSession, Model m) {
		
		String name = p.getName(); 
		User u = userDao.getUserByUserName(name);
		Contact persistedContact = contactManagement.getContactById(contact.getContactId());
		try {
			if(!image.isEmpty()) {
				File file;
				
				file = new ClassPathResource("static/img").getFile();
				 
				Path path = Paths.get(file.getAbsolutePath()+File.separator+persistedContact.getContactImage());
				Path newPath = Paths.get(file.getAbsolutePath()+File.separator+image.getOriginalFilename());
				Files.delete(path);
				Files.copy(image.getInputStream(), newPath, StandardCopyOption.REPLACE_EXISTING);
				contact.setContactImage(image.getOriginalFilename());
			} else {
				contact.setContactImage(persistedContact.getContactImage());
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		contact.setUser(u);
		contactManagement.saveContactInfo(contact);
		httpSession.setAttribute("message", "contact updated successfully");
		return getContactToUpdateById(contact.getContactId(),m,p);
	}
	
}
