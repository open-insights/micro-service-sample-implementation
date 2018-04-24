package com.oi.hermes.authentication.gateway.security.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oi.hermes.authentication.gateway.security.config.WebSecurityConfig;
import com.oi.hermes.authentication.gateway.security.dao.AuthenticationConfigRepository;
import com.oi.hermes.authentication.gateway.security.entity.AuthConfig;

@Service
public class CurrentAuthenticationConfigService {
	public static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);
	@Autowired
	AuthenticationConfigRepository authenticationConfigRepository;

	AuthConfig configdata;

	public AuthConfig initAuthConfig() {
		LOGGER.info("Retriving authentication configuration details");
		authenticationConfigRepository.findByEnabled(true).forEach(config -> configdata = config);
		LOGGER.info("Authentication configuration details" + configdata);
		return configdata;
	}

	public AuthConfig getAuthConfig() {
		LOGGER.info("Injecting authentication configuration details to security context");
		return configdata;
	}
}
