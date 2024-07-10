package com.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.exceptions.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {
	
	@Autowired
	private ContactRepo contactRepo;
	
	@Override
	public Contact save(Contact contact) {
		// TODO Auto-generated method stub
		String contactId = UUID.randomUUID().toString();
		contact.setId(contactId);
		return contactRepo.save(contact);
	}

	@Override
	public Contact update(Contact contact) {
		// TODO Auto-generated method stub
		Contact oldContact = contactRepo.findById(contact.getId()).orElseThrow(() -> new ResourceNotFoundException("Contact does not exist"));
		oldContact.setName(contact.getName());
		oldContact.setEmail(contact.getEmail());
		oldContact.setAddress(contact.getAddress());
		oldContact.setDescription(contact.getDescription());
		oldContact.setPhoneNumber(contact.getPhoneNumber());
		oldContact.setFavorite(contact.isFavorite());
		oldContact.setWebsiteLink(contact.getWebsiteLink());
		oldContact.setLinkedinLink(contact.getLinkedinLink());
		oldContact.setPicture(contact.getPicture());
		oldContact.setPublicId(contact.getPublicId());
		
		return contactRepo.save(oldContact);
	}

	@Override
	public List<Contact> getAll() {
		// TODO Auto-generated method stub
		return contactRepo.findAll();
	}

	@Override
	public Contact getById(String id) {
		// TODO Auto-generated method stub
		return contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact with "+id+" doesn't exist"));
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		Contact contact = contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact with "+id+" doesn't exist"));
		contactRepo.delete(contact);
	}

	@Override
	public List<Contact> getByUserId(String userId) {
		// TODO Auto-generated method stub
		return contactRepo.findByUserId(userId);
	}

	@Override
	public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {
		// TODO Auto-generated method stub
		Sort sort = direction.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(page,size);
		
		return contactRepo.findByUser(user, pageable);
	}

	@Override
	public Page<Contact> searchByName(User user, String name, int page, int size, String sortBy, String direction) {
		// TODO Auto-generated method stub
		Sort sort = direction.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return contactRepo.findByUserAndNameContaining(user, name, pageable);
	}

	@Override
	public Page<Contact> searchByEmail(User user, String email, int page, int size, String sortBy, String direction) {
		// TODO Auto-generated method stub
		Sort sort = direction.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return contactRepo.findByUserAndEmailContaining(user, email, pageable);
	}

	@Override
	public Page<Contact> searchByPhoneNumber(User user, String phoneNumber, int page, int size, String sortBy, String direction) {
		// TODO Auto-generated method stub
		Sort sort = direction.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumber, pageable);
	}

}
