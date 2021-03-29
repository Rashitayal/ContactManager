package com.springboot.ContactManager.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.ContactManager.entities.Contact;

public interface ContactDao extends JpaRepository<Contact, Integer>{
	
	@Query("select c from Contact c where c.user.userId = :userId")
	public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);
	
	public Contact findByContactId(@Param("contactId") int contactId);
	
	@Query("select c from Contact c where concat(c.firstName,' ',c.lastName) like %:keyword% and c.user.userId = :userId")
	public List<Contact> findContactsByKeyword(@Param("keyword") String keyword, @Param("userId")int userId);

}
