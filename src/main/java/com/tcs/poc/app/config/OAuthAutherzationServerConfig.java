package com.tcs.poc.app.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
@EnableAuthorizationServer
public class OAuthAutherzationServerConfig extends AuthorizationServerConfigurerAdapter{
	
	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private com.tcs.poc.app.service.UserDetailServiceImpl detailServiceImpl;
	
	@Autowired
	private Environment env;
	
	@Autowired 
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()")
				.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager)
		.userDetailsService(detailServiceImpl)
		.tokenStore(tokenStore())
		.accessTokenConverter(accessTokenConverter())
		;
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		String secretEncoded = passwordEncoder.encode(env.getProperty("security.oauth2.client.clientSecret"));
		clients.inMemory()
		.withClient(env.getProperty("security.oauth2.client.clientId"))
		.secret(secretEncoded)		
		.authorizedGrantTypes("password","authorization_code","refresh_token")
		.scopes("read","write")
		.autoApprove(true)
		.accessTokenValiditySeconds(7200);
	}
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("rts_signing_key");
		return converter;
	}

}
