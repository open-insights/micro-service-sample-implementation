package com.oi.hermes.authentication.gateway.security.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Id;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.oi.hermes.authentication.gateway.security.entity.AuthConfig;
@Component
public class AuthConfiguration extends AuthConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String authType;
	private Map<String, String> authPropertyMap;
	public AuthConfiguration() {
		authPropertyMap = new HashMap<String, String>();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public Map<String, String> getAuthPropertyMap() {
		return authPropertyMap;
	}

	public void setAuthPropertyMap(Map<String, String> authPropertyMap) {
		this.authPropertyMap = authPropertyMap;
	}


}
