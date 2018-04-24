package com.oi.hermes.customer.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.oi.hermes.customer.exception.ExceptionResponseBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryChannelPoint implements AuthenticationEntryPoint, Serializable {
	public static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryChannelPoint.class);
	private static final long serialVersionUID = -8970718410437077606L;

	@Autowired
	ExceptionResponseBuilder exceptionResponseBuilder;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws AuthenticationException, IOException {
		response.setContentType("application/json");
	}
}