package com.leolimajr.sample.config;

import com.leolimajr.sample.service.SecurityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@Profile({ ApplicationConfig.ConfigProfile.MAIN,
		ApplicationConfig.ConfigProfile.ACCEPTANCE_TEST,
		ApplicationConfig.ConfigProfile.WEB_INTEGRATION_TEST })
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityService securityService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/css/**", "/", "/signup").permitAll()
				.anyRequest().authenticated().and().formLogin().loginPage("/login")
				.failureUrl("/login?error").permitAll().and().logout();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.securityService);
	}

}