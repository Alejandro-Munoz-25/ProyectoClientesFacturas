package com.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springboot.app.auth.handler.LoginSuccessHandler;
import com.springboot.app.models.service.JpaUserDetailService;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LoginSuccessHandler succeshandler;

	@Autowired
	private JpaUserDetailService jpaUserDetailService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(jpaUserDetailService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "/CSS/**", "/Javascript/**", "/images/**", "/list**", "/api/customers/list",
						"/locale", "/404")
				.permitAll().anyRequest().authenticated()
				.and().formLogin().successHandler(succeshandler).loginPage("/login").permitAll().and().logout()
				.permitAll();
	}

}
