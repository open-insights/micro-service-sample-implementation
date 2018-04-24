package com.oi.hermes.authentication.gateway.security.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oi.hermes.authentication.gateway.exception.type.InvalidCredentialsException;
import com.oi.hermes.authentication.gateway.security.*;
import com.oi.hermes.authentication.gateway.security.entity.Authority;
import com.oi.hermes.authentication.gateway.security.entity.AuthorityName;
import com.oi.hermes.authentication.gateway.security.entity.User;
import com.oi.hermes.authentication.gateway.security.model.HermesUser;
import com.oi.hermes.authentication.gateway.security.model.HermesUserFactory;
import com.oi.hermes.authentication.gateway.security.model.TokenAuthenticationRequest;
import com.oi.hermes.authentication.gateway.security.service.*;

import brave.Tracer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationRestControllerTest {

	private MockMvc mvc;

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private AuthenticationManager authenticationManager;
	@MockBean
	private JwtTokenUtil jwtTokenUtil;

	@MockBean
	private JwtUserDetailsService jwtUserDetailsService;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	@WithAnonymousUser
	public void successfulAuthenticationWithAnonymousUser() throws Exception {

		TokenAuthenticationRequest jwtAuthenticationRequest = new TokenAuthenticationRequest("admin", "admin");
		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				jwtAuthenticationRequest.getUsername(), jwtAuthenticationRequest.getPassword())))
						.thenReturn(SecurityContextHolder.getContext().getAuthentication());
		mvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(jwtAuthenticationRequest))).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "USER")
	public void successfulRefreshTokenWithUserRole() throws Exception {

		Authority authority = new Authority();
		authority.setId(0L);
		authority.setName(AuthorityName.ROLE_USER);
		List<Authority> authorities = Arrays.asList(authority);

		User user = new User();
		user.setUsername("admin");
		user.setAuthorities(authorities);
		user.setEnabled(Boolean.TRUE);
		user.setLastPasswordResetDate(new Date(System.currentTimeMillis() + 1000 * 1000));

		HermesUser jwtUser = HermesUserFactory.create(user);

		when(jwtTokenUtil.getUsernameFromToken(any())).thenReturn(user.getUsername());

		when(jwtUserDetailsService.loadUserByUsername(eq(user.getUsername()))).thenReturn(jwtUser);

		when(jwtTokenUtil.canTokenBeRefreshed(any(), any())).thenReturn(true);

		mvc.perform(get("/refresh").header("Authorization", "Bearer 5d1103e-b3e1-4ae9-b606-46c9c1bc915a"))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void successfulRefreshTokenWithAdminRole() throws Exception {

		Authority authority = new Authority();
		authority.setId(1L);
		authority.setName(AuthorityName.ROLE_ADMIN);
		List<Authority> authorities = Arrays.asList(authority);

		User user = new User();
		user.setUsername("admin");
		user.setAuthorities(authorities);
		user.setEnabled(Boolean.TRUE);
		user.setLastPasswordResetDate(new Date(System.currentTimeMillis() + 1000 * 1000));

		HermesUser jwtUser = HermesUserFactory.create(user);

		when(jwtTokenUtil.getUsernameFromToken(any())).thenReturn(user.getUsername());

		when(jwtUserDetailsService.loadUserByUsername(eq(user.getUsername()))).thenReturn(jwtUser);

		when(jwtTokenUtil.canTokenBeRefreshed(any(), any())).thenReturn(true);

		mvc.perform(get("/refresh").header("Authorization", "Bearer 5d1103e-b3e1-4ae9-b606-46c9c1bc915a"))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	@WithAnonymousUser
	public void shouldGetUnauthorizedWithAnonymousUser() throws Exception {

		mvc.perform(get("/refresh").header("Authorization", "Bearer 5d1103e-b3e1-4ae9-b606-46c9c1bc915a")).andExpect(status().is3xxRedirection());
	}
}
