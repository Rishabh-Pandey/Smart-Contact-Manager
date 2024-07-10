package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/")
	public String index() {
		return "redirect:/home";
	}
	
	@RequestMapping("/home")
	public String home() {
		return "home";
	}
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	
	@GetMapping("/service")
	public String service() {
		return "service";
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		UserForm userForm = new UserForm();
		model.addAttribute("userForm", userForm);
		return "register";
	}
	
	@RequestMapping(value="/doRegister",method=RequestMethod.POST)
	public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult ,HttpSession httpSession) {
		
		if(bindingResult.hasErrors()) {
			return "register";
		}
		
		User user = new User();
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		user.setPassword(userForm.getPassword());
		user.setAbout(userForm.getAbout());
		user.setPhoneNumber(userForm.getPhoneNumber());
		userService.saveUser(user);
		Message message = Message.builder()
				.content("Registration Successful")
				.type(MessageType.green)
				.build();
		httpSession.setAttribute("message", message);
		return "redirect:/register";
	}
}
