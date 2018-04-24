package com.oi.hermes.subscription.security;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.SignatureException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oi.hermes.subscription.exception.ExceptionResponseBuilder;
import com.oi.hermes.subscription.security.controller.HermesUser;
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
	public static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryChannelPoint.class);
	private JwtTokenUtil jwtTokenUtil;
	private String tokenHeader;
	private ExceptionResponseBuilder exceptionResponseBuilder;
	private ObjectMapper mapper;

	public JwtAuthorizationTokenFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil,
			String tokenHeader, ExceptionResponseBuilder exceptionResponseBuilder, ObjectMapper mapper) {
		this.jwtTokenUtil = jwtTokenUtil;
		this.tokenHeader = tokenHeader;
		this.exceptionResponseBuilder = exceptionResponseBuilder;
		this.mapper = mapper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		LOGGER.info("processing authentication for '{}'", request.getRequestURL());

		final String requestHeader = request.getHeader(this.tokenHeader);
		LOGGER.info("headers==================>"+request.getHeaderNames());
		String username = null;
		String authToken = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			authToken = requestHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(authToken);
			} catch (SignatureException ex) {
				LOGGER.info("Invalid token");
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				try {
					response.getOutputStream().println(
							mapper.writeValueAsString(exceptionResponseBuilder.buildExceptionResponse(ex, request)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IllegalArgumentException ex) {
				LOGGER.info("an error occured during getting username from token");
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				try {
					response.getOutputStream().println(
							mapper.writeValueAsString(exceptionResponseBuilder.buildExceptionResponse(ex, request)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ExpiredJwtException ex) {
				LOGGER.info("the token is expired and not valid anymore");
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				try {
					response.getOutputStream().println(
							mapper.writeValueAsString(exceptionResponseBuilder.buildExceptionResponse(ex, request)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			LOGGER.info("couldn't find bearer string, will ignore the header");
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			try {
				response.getOutputStream().println(mapper.writeValueAsString(exceptionResponseBuilder
						.buildExceptionResponse(new Exception("Couldn't find valid token"), request)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		LOGGER.info("checking authentication for user '{}'", username);
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			LOGGER.info("security context was null, so authorizating user");

			HermesUser user = new HermesUser();
			try {
				user.setFirstname(username);
				Claims claims = jwtTokenUtil.getTokenClaims(authToken);
				Collection<Object> r = claims.values();
				SimpleGrantedAuthority authority = null;
				List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();

				for (Object autority : r) {
					authority = new SimpleGrantedAuthority(autority.toString());
					updatedAuthorities.add(authority);
				}

				// userDetails = this.userDetailsService.loadUserByUsername(username);
				// authenticating user from token itself. NOt required to verify it from
				// DB/delegated backend
				if (!(jwtTokenUtil.validateToken(authToken))) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							user, null, updatedAuthorities);
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					LOGGER.info("authorizated user '{}', setting security context", username);
					SecurityContextHolder.getContext().setAuthentication(authentication);
					chain.doFilter(request, response);
				}
			} catch (UsernameNotFoundException ex) {
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				try {
					response.getOutputStream().println(
							mapper.writeValueAsString(exceptionResponseBuilder.buildExceptionResponse(ex, request)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}
}
