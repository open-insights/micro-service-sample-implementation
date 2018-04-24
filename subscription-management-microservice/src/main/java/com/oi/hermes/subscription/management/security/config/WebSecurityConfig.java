package com.oi.hermes.subscription.management.security.config;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oi.hermes.subscription.management.exception.ExceptionResponseBuilder;
import com.oi.hermes.subscription.management.security.JwtAuthenticationEntryChannelPoint;
import com.oi.hermes.subscription.management.security.JwtAuthorizationTokenFilter;
import com.oi.hermes.subscription.management.security.JwtTokenUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	public static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);
	@Autowired
	private JwtAuthenticationEntryChannelPoint unauthorizedHandler;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	ExceptionResponseBuilder exceptionResponseBuilder;
	@Autowired
	ObjectMapper objectMapper;
	@Value("${jwt.header}")
	private String tokenHeader;


	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().antMatchers("/h2-console/**/**").permitAll().antMatchers("/**").permitAll()
				.anyRequest().authenticated();

		JwtAuthorizationTokenFilter authenticationTokenFilter = new JwtAuthorizationTokenFilter(userDetailsService(),
				jwtTokenUtil, tokenHeader, exceptionResponseBuilder, objectMapper);
		httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
		httpSecurity.headers().frameOptions().sameOrigin().cacheControl();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.POST, "/api/**","/api/**/**", "/users").and().ignoring()
				.antMatchers(HttpMethod.GET, "/swagger-ui.html/**", "/*.html", "/favicon.ico", "/**/*.html",
						"/**/*.css", "/**/*.js", "/**/*.ts", "/**/*.scss", "/webjars/springfox-swagger-ui/**/*.*",
						"/v2/api-docs", "/swagger-resources", "/swagger-resources/configuration/**")
				.and().ignoring().antMatchers("/h2-console/**/**").antMatchers(HttpMethod.GET, "/users/**");
	}
}
