package com.oi.hermes.authentication.gateway.security.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.oi.hermes.authentication.gateway.security.entity.Authority;
import com.oi.hermes.authentication.gateway.security.entity.User;

public final class HermesUserFactory {

	private HermesUserFactory() {
	}

	public static HermesUser create(User user) {
		return new HermesUser(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(),
				user.getEmail(), user.getPassword(), mapToGrantedAuthorities(user.getAuthorities()), user.getEnabled(),
				user.getLastPasswordResetDate());
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities){
		return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
				.collect(Collectors.toList());
	}
}
