package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactForm {
	@NotBlank(message="Name is required")
	private String name;
	
	@Email(message="Invalid email")
	private String email;
	
	@NotBlank(message="Phone Number is required")
	@Pattern(regexp = "^[0-9]{10}$",message="Invalid Phone number")
	private String phoneNumber;
	
	@NotBlank(message="Address is required")
	private String address;
	private String description;
	private boolean favorite;
	private String websiteLink;
	private String linkedinLink;
	
	@ValidFile
	private MultipartFile picture;
	
	private String pictureUrl;
}
