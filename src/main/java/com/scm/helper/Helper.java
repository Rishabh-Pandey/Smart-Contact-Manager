package com.scm.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class Helper {
	
	public static String getEmailOfLoggedInUser(Authentication authentication) {
		String loggedInUserEmail = "";
		
		if(authentication instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
			DefaultOAuth2User oAuth2User = (DefaultOAuth2User)authentication.getPrincipal();
			
			if(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equalsIgnoreCase("google")) {
				loggedInUserEmail = oAuth2User.getAttribute("email").toString();
			}
			else if(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equalsIgnoreCase("github")) {
				loggedInUserEmail = (oAuth2User.getAttribute("email")!=null)?oAuth2User.getAttribute("email").toString():oAuth2User.getAttribute("login").toString()+"@gmail.com";
			}
		}
		else {
			loggedInUserEmail = authentication.getName();
		}
		
		return loggedInUserEmail;
	}
	
}
