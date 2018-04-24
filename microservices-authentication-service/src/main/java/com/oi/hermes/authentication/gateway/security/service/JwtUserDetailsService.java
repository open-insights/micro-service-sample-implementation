package com.oi.hermes.authentication.gateway.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oi.hermes.authentication.gateway.exception.type.EntityAlreadyPresentException;
import com.oi.hermes.authentication.gateway.security.controller.AuthenticationRestController;
import com.oi.hermes.authentication.gateway.security.dao.UserRepository;
import com.oi.hermes.authentication.gateway.security.entity.User;
import com.oi.hermes.authentication.gateway.security.model.HermesUserFactory;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	public static final Logger LOGGER = LoggerFactory.getLogger(JwtUserDetailsService.class);
	@Autowired
	private UserRepository userRepository;
 	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			LOGGER.error(String.format("No user found with username '%s'.", username));
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return HermesUserFactory.create(user);
		}
	}
}
