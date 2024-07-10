package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.repositories.UserRepo;
import com.scm.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OauthAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	
	@Autowired
	private UserRepo userRepo;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		//save user info in DB
		
		OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken)authentication;
		
		String authorizedClientRegistrationId = authenticationToken.getAuthorizedClientRegistrationId();
		
		DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal();
		
		User user1 = new User();
		
		user1.setUserId(UUID.randomUUID().toString());
		user1.setPassword("password");
		user1.setRoleList(List.of(AppConstants.ROLE_USER));
		user1.setEmailVerified(true);
		
		if(authorizedClientRegistrationId.equalsIgnoreCase("google")) {
			String name=user.getAttribute("name").toString();
			String email=user.getAttribute("email").toString();
			String picture = user.getAttribute("picture").toString();
			
			user1.setName(name);
			user1.setEmail(email);
			user1.setProfilePic(picture);
			user1.setProvider(Providers.GOOGLE);
			user1.setProviderUserId(user.getName());
		}
		else if(authorizedClientRegistrationId.equalsIgnoreCase("github")) {
			String name = user.getAttribute("login").toString();
			String email = (user.getAttribute("email")!=null)?user.getAttribute("email").toString():user.getAttribute("login").toString()+"@gmail.com";
			String picture = user.getAttribute("avatar_url").toString();
			String about = (user.getAttribute("bio")!=null)?user.getAttribute("bio").toString():null;
			
			user1.setName(name);
			user1.setEmail(email);
			user1.setProfilePic(picture);
			user1.setProvider(Providers.GITHUB);
			user1.setProviderUserId(user.getName());
			user1.setAbout(about);
		}
		
		if(!userRepo.findByEmail(user1.getEmail()).isPresent()) {
			userRepo.save(user1);
		}
		
		new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
	}

}
