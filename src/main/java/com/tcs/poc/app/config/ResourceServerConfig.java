package com.tcs.poc.app.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	
	@Autowired
	private Environment env;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(env.getProperty("security.oauth2.client.clientId"));
		
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
//		http
//			.csrf()
//				.disable()
//			.formLogin()
//				.disable()
//			.httpBasic()
//				.disable()
//			.sessionManagement()
//			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//			.and()
//			.authorizeRequests()
//			.anyRequest()
//			.authenticated();
		http
		.csrf().disable()
		.authorizeRequests().antMatchers("/oauth/token","/login","/service/register-user","/forgotPasswordByQuestion","/forgotPasswordByOtp","/VerifyOtp").permitAll()
//		.antMatchers("/Admin").hasRole("ADMIN")
//		.antMatchers("/User").hasRole("USER")
		.antMatchers("/Admin").hasRole("ADMIN")
		.antMatchers("/sendMoney").hasRole("CUSTOMER")
		.antMatchers("/Employee").hasRole("EMPLOYEE")
		.anyRequest().authenticated()
		.and()
		.cors();
	}
}
