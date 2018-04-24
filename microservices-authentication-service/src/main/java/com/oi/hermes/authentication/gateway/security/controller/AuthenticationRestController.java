package com.oi.hermes.authentication.gateway.security.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oi.hermes.authentication.gateway.exception.type.InvalidCredentialsException;
import com.oi.hermes.authentication.gateway.security.JwtTokenUtil;
import com.oi.hermes.authentication.gateway.security.model.HermesUser;
import com.oi.hermes.authentication.gateway.security.model.JwtAuthenticationResponse;
import com.oi.hermes.authentication.gateway.security.model.TokenAuthenticationRequest;

@RestController
public class AuthenticationRestController {
	public static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationRestController.class);
	@Value("${jwt.header}")
	private String tokenHeader;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	@Qualifier("jwtUserDetailsService")
	private UserDetailsService userDetailsService;

	@RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody TokenAuthenticationRequest authenticationRequest) {
		LOGGER.info("received create token api request");
		try {
			final Authentication authentication = authenticate(authenticationRequest.getUsername(),
					authenticationRequest.getPassword());
			if (authentication.getPrincipal() != null && authentication.getPrincipal().toString().equals("anonymous")) {
				throw new InvalidCredentialsException("Invalid User access" + authentication.getPrincipal().toString());
			} else if (authentication.getPrincipal() != null) {
				UserDetails c = (UserDetails) authentication.getPrincipal();
				String token = jwtTokenUtil.generateToken(c);
				LOGGER.info("token return response");
				return ResponseEntity.ok(new JwtAuthenticationResponse(token));
			}

			// Return the token
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException(e, e.getMessage());
		}
		return null;
	}

	@RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		LOGGER.info("refresh token api request received");
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		HermesUser user = (HermesUser) userDetailsService.loadUserByUsername(username);

		if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@ExceptionHandler({ UsernameNotFoundException.class })
	public ResponseEntity<String> handleAuthenticationException(UsernameNotFoundException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	/**
	 * Authenticates the user. If something is wrong, an
	 * {@link AuthenticationException} will be thrown
	 * 
	 * @return
	 */
	private Authentication authenticate(String username, String password) {
		LOGGER.info("Authenticates the user");
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		Authentication userDetails = null;
		try {
			userDetails = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			LOGGER.error("User is disabled!");
			throw new BadCredentialsException("User is disabled!", e);
		} catch (BadCredentialsException e) {
			LOGGER.error("Bad credentials!");
			throw new BadCredentialsException("Bad credentials!", e);
		}
		return userDetails;
	}
}
