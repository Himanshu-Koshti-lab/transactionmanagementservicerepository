package com.tcs.poc.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tcs.poc.app.service.UserDetailServiceImpl;



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private UserDetailServiceImpl detailServiceImpl;
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors();
//		http
//			.sessionManagement()
//			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.and()
//			.authorizeRequests()
//			.antMatchers("/login","/oauth/token")
//			.permitAll()
//			.anyRequest()
//			.authenticated();
		
//		http
//			.csrf().disable()
//			.authorizeRequests().antMatchers("/oauth/token","/login").permitAll()
//			.anyRequest().authenticated();
		
//		.csrf()
//		.disable()
//		.sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//		.and()
//		.authorizeRequests()
//		.antMatchers("/login","/oauth/token")
//		.permitAll()
//		.anyRequest()
//		.authenticated();
	}
	
	
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(detailServiceImpl).passwordEncoder(passwordEncoder());
		  //auth.inMemoryAuthentication().withUser("User").password(bCryptPasswordEncoder().encode("user")).roles("USER");
		  //auth.inMemoryAuthentication().withUser("Admin").password(bCryptPasswordEncoder().encode("admin")).roles("ADMIN");
	}

}
