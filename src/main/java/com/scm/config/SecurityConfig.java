package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails user1 = User.withDefaultPasswordEncoder().username("rishabh").password("rishabh").roles("ADMIN","USER").build();
//		UserDetails user2 = User.withDefaultPasswordEncoder().username("rishabh123").password("rishabh123").roles("ADMIN","USER").build();
//		return new InMemoryUserDetailsManager(user1,user2);
//	}
	
	@Autowired
	private SecurityCustomUserDetailService userDetailService;
	
	@Autowired
	private OauthAuthenticationSuccessHandler oauthAuthenticationSuccessHandler;
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}
	
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//		return authenticationConfiguration.getAuthenticationManager();
//	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.authorizeHttpRequests(authorize -> {
			authorize.requestMatchers("/user/**").authenticated();
			authorize.anyRequest().permitAll();
		});
		
		httpSecurity.formLogin(formLogin -> {
			formLogin.loginPage("/login");
			formLogin.loginProcessingUrl("/authenticate");
			formLogin.defaultSuccessUrl("/user/dashboard",true);
					// .failureForwardUrl("/login?error=true");
			
			formLogin.usernameParameter("email");
//			formLogin.passwordParameter("password");
		});
		
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		
		httpSecurity.logout(logoutForm -> {
			logoutForm.logoutUrl("/do-logout");
			logoutForm.logoutSuccessUrl("/login?logout=true");
		});
		
		httpSecurity.oauth2Login(oauth -> {
			oauth.loginPage("/login");
			oauth.successHandler(oauthAuthenticationSuccessHandler);
		});
		
//		httpSecurity.exceptionHandling(exception -> exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));
		
		return httpSecurity.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
