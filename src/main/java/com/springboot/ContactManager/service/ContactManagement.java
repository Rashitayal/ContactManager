package com.springboot.ContactManager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.springboot.ContactManager.dao.ContactDao;
import com.springboot.ContactManager.entities.Contact;

@Service
public class ContactManagement {

	@Autowired
	private ContactDao contactDao;
	
	public Contact saveContactInfo(Contact c) {
		contactDao.save(c);
		return c;
	}
	
	public Page<Contact> getAllContacts(int userId, PageRequest p){
		return contactDao.findContactsByUser(userId,p);
	}
	
	public Contact getContactById(int contactId) {
		return contactDao.findByContactId(contactId);
	}
	
	public void deleteContactById(Contact c) {
		contactDao.delete(c);
	}
	
	public List<Contact> findContactByKeyword(String keyword, int userId){
		return contactDao.findContactsByKeyword(keyword, userId);
	}
}
