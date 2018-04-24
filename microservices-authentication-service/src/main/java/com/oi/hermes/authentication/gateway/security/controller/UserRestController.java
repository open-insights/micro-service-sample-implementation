package com.oi.hermes.authentication.gateway.security.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oi.hermes.authentication.gateway.security.dao.UserRepository;
import com.oi.hermes.authentication.gateway.security.entity.User;
import com.oi.hermes.authentication.gateway.security.model.HermesUser;
import com.oi.hermes.authentication.gateway.security.model.HermesUserFactory;
import com.oi.hermes.authentication.gateway.security.service.UserService;

@BasePathAwareController
public class UserRestController {
	public static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
	@Value("${jwt.header}")
	private String tokenHeader;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserService userService;
	/**
	 * Returns user details with its authorities that assigned.
	 *
	 * @param User
	 *            object
	 * @return User which is registered for same instance
	 * @see Image
	 */

	@RequestMapping(path = "users", method = RequestMethod.POST, produces = "application/hal+json")
    @ResponseBody
	public HermesUser registerUser(@Valid @RequestBody User user) {
		LOGGER.info("/create-user");
		User hermesUser = null;
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		hermesUser = userService.save(user);
		return HermesUserFactory.create(hermesUser);
	}
}
