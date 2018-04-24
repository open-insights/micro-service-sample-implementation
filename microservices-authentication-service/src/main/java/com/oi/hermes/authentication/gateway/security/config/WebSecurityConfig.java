package com.oi.hermes.authentication.gateway.security.config;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oi.hermes.authentication.gateway.exception.ExceptionResponseBuilder;
import com.oi.hermes.authentication.gateway.security.entity.AuthConfig;
import com.oi.hermes.authentication.gateway.security.service.CurrentAuthenticationConfigService;
import com.oi.hermes.authentication.gateway.security.service.JwtUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	public static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);
	@Autowired
	CurrentAuthenticationConfigService currentAuthenticationConfigService;

	@Autowired
	ExceptionResponseBuilder exceptionResponseBuilder;
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	@Autowired
	ObjectMapper objectMapper;
	@Value("${jwt.header}")
	private String tokenHeader;
	@Value("${jwt.route.authentication.path}")
	private String authenticationPath;
	@Autowired

	@PostConstruct
	public AuthConfig initAuthConfiguration() {
		return currentAuthenticationConfigService.initAuthConfig();
	}

	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		AuthConfig authConfig = currentAuthenticationConfigService.getAuthConfig();
		if (authConfig == null) {
			LOGGER.info("Setting up database authentication builder config");
			auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoderBean());
		} else {
			if (authConfig.getAuthType().equalsIgnoreCase("ldap")) {
				LOGGER.info("Setting up LDAP authentication builder config");
				auth.ldapAuthentication().contextSource().url(authConfig.getAuthPropertyMap().get("url"))
						.managerDn(authConfig.getAuthPropertyMap().get("managerDn"))
						.managerPassword(authConfig.getAuthPropertyMap().get("managerPassword")).and()
						.userDnPatterns(authConfig.getAuthPropertyMap().get("userDnPatterns"));
			}
		}
	}

	@Bean
	public PasswordEncoder passwordEncoderBean() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.POST, authenticationPath, "/api/**", "/api/**/**", "/api/**/**/**").and()
				.ignoring().and().ignoring().antMatchers(HttpMethod.PUT, "/api/**/**").and().ignoring()
				.antMatchers(HttpMethod.DELETE, "/api/**/**")
				.antMatchers(HttpMethod.GET, "/api/**", "/api/**/**", "/swagger-ui.html/**", "/*.html", "/favicon.ico",
						"/**/*.html", "/**/*.css", "/**/*.js", "/**/*.ts", "/**/*.scss",
						"/webjars/springfox-swagger-ui/**/*.*", "/v2/api-docs", "/swagger-resources",
						"/swagger-resources/configuration/**")
				.and().ignoring().antMatchers("/h2-console/**/**").antMatchers(HttpMethod.GET, "/users/**");
	}
}
