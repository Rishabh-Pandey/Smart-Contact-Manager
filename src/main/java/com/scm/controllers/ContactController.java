package com.scm.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ImageService imageService;
	
	@GetMapping("/add")
	public String addContact(Model model) {
		
		ContactForm contactForm = new ContactForm();
		
		model.addAttribute("contactForm", contactForm);
		
		return "user/add_contact";
	}
	
	@PostMapping("/add")
	public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult , Authentication authentication, HttpSession session) {
		
		if(bindingResult.hasErrors()) {
			return "user/add_contact";
		}
		
		String username = Helper.getEmailOfLoggedInUser(authentication);
		
		String filename = UUID.randomUUID().toString();
		
		String fileUrl = imageService.uploadImage(contactForm.getPicture(),filename);
		
		Contact contact = new Contact();
		contact.setName(contactForm.getName());
		contact.setEmail(contactForm.getEmail());
		contact.setPhoneNumber(contactForm.getPhoneNumber());
		contact.setAddress(contactForm.getAddress());
		contact.setDescription(contactForm.getDescription());
		contact.setWebsiteLink(contactForm.getWebsiteLink());
		contact.setLinkedinLink(contactForm.getLinkedinLink());
		contact.setFavorite(contactForm.isFavorite());
		contact.setUser(userService.getUserByEmail(username));
		contact.setPicture(fileUrl);
		contact.setPublicId(filename);
		
		contactService.save(contact);
		
		session.setAttribute("message", new Message().builder().content("Contact added successfully").type(MessageType.green).build());
		
		return "redirect:/user/contacts/add";
	}
	
	@GetMapping()
	public String viewContacts(@RequestParam(value="page", defaultValue = "0") int page, 
							   @RequestParam(value="size", defaultValue = AppConstants.PAGE_SIZE+"") int size, 
							   @RequestParam(value="sortBy", defaultValue = "name") String sortBy, 
							   @RequestParam(value="direction", defaultValue = "aesc") String direction, Authentication authentication, Model model) {
		
		String username = Helper.getEmailOfLoggedInUser(authentication);
		
		User user = userService.getUserByEmail(username);
		
		Page<Contact> contacts = contactService.getByUser(user,page,size,sortBy,direction);
		
		model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
		model.addAttribute("contacts", contacts);
		model.addAttribute("contactSearchForm", new ContactSearchForm());
		
		return "user/contacts";
	}
	
	@GetMapping("/search")
	public String searchHandler(
			@ModelAttribute ContactSearchForm contactSearchForm,
			@RequestParam(value="page", defaultValue = "0") int page, 
			@RequestParam(value="size", defaultValue = AppConstants.PAGE_SIZE+"") int size, 
			@RequestParam(value="sortBy", defaultValue = "name") String sortBy, 
			@RequestParam(value="direction", defaultValue = "aesc") String direction,
			Model model,
			Authentication authentication
		) {
		
		String field = contactSearchForm.getField();
		String keyword = contactSearchForm.getKeyword();
		
		String username = Helper.getEmailOfLoggedInUser(authentication);
		
		User user = userService.getUserByEmail(username);
		
		Page<Contact> contacts=null;
		if(field.equalsIgnoreCase("name")) {
			contacts=contactService.searchByName(user, keyword, page, size, sortBy, direction);
		}
		else if(field.equalsIgnoreCase("email")) {
			contacts=contactService.searchByEmail(user, keyword, page, size, sortBy, direction);
		}
		else if(field.equalsIgnoreCase("phone")) {
			contacts=contactService.searchByPhoneNumber(user, keyword, page, size, sortBy, direction);
		}
		
		model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
		model.addAttribute("contacts", contacts);
		model.addAttribute("contactSearchForm", contactSearchForm);
		
		return "user/search";
	}
	
	@GetMapping("/delete/{contactId}")
	public String deleteContact(@PathVariable String contactId, HttpSession session) {
		
		contactService.delete(contactId);
		
		session.setAttribute("message", new Message().builder().content("Contact Deleted Successfully").type(MessageType.red).build());
		
		return "redirect:/user/contacts";
	}
	
	@GetMapping("/view/{contactId}")
	public String updateContactFormView(@PathVariable("contactId") String contactId, Model model) {
		
		Contact contact = contactService.getById(contactId);
		ContactForm contactForm = new ContactForm();
		contactForm.setName(contact.getName());
		contactForm.setEmail(contact.getEmail());
		contactForm.setPhoneNumber(contact.getPhoneNumber());
		contactForm.setAddress(contact.getAddress());
		contactForm.setDescription(contact.getDescription());
		contactForm.setWebsiteLink(contact.getWebsiteLink());
		contactForm.setLinkedinLink(contact.getLinkedinLink());
		contactForm.setFavorite(contact.isFavorite());
		contactForm.setPictureUrl(contact.getPicture());
		
		model.addAttribute("contactForm", contactForm);
		model.addAttribute("contactId", contactId);
		return "user/update_contact_view";
	}
	
	@PostMapping("/update/{contactId}")
	public String updateContact(@PathVariable("contactId") String contactId, @ModelAttribute ContactForm contactForm) {
		
		Contact contact = contactService.getById(contactId);
		contact.setName(contactForm.getName());
		contact.setEmail(contactForm.getEmail());
		contact.setPhoneNumber(contactForm.getPhoneNumber());
		contact.setAddress(contactForm.getAddress());
		contact.setDescription(contactForm.getDescription());
		contact.setWebsiteLink(contactForm.getWebsiteLink());
		contact.setLinkedinLink(contactForm.getLinkedinLink());
		contact.setFavorite(contactForm.isFavorite());
		//contact.setPicture(null);
		//contact.setPublicId(null);
		
		if(contactForm.getPicture()!=null && !contactForm.getPicture().isEmpty()) {
			String filename = UUID.randomUUID().toString();
			String fileUrl = imageService.uploadImage(contactForm.getPicture(),filename);
			contact.setPicture(fileUrl);
			contact.setPublicId(filename);
		}
		
		contactService.update(contact);
		
		return "redirect:/user/contacts";
	}
	
}
