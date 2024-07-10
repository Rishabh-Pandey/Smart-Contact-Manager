package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForm {
	@NotBlank(message="Username is rquired")
	@Size(min=3,message="Min 3 characters required")
	private String name;
	
	@Email(message="Invalid email address")
	private String email;
	
	@NotBlank(message="Password is required")
	@Size(min=6, message="Min 6 characters are required")
	private String password;
	
	private String about;
	
	@Size(min=8, max=12, message="Min 8 characters and maximum 12 characters are allowed")
	private String phoneNumber;
}
