package com.oi.hermes.authentication.gateway.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oi.hermes.authentication.gateway.exception.type.EntityAlreadyPresentException;
import com.oi.hermes.authentication.gateway.security.dao.UserRepository;
import com.oi.hermes.authentication.gateway.security.entity.User;


@Service
public class UserService {
	public static final Logger LOGGER = LoggerFactory.getLogger(JwtUserDetailsService.class);
	@Autowired
	private UserRepository userRepository;

	public User save(User user) throws EntityAlreadyPresentException {
		LOGGER.info("/create-user");
		User hermesUser = null;
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		try {
			hermesUser = userRepository.save(user);
		} catch (DataIntegrityViolationException ex) {
			throw new EntityAlreadyPresentException(ex.getMessage(), ex);
		}

		return hermesUser;
	}
}
