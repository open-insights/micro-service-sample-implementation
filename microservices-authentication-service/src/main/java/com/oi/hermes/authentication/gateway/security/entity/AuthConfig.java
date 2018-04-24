package com.oi.hermes.authentication.gateway.security.entity;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;

@Entity
public class AuthConfig {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String authType;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "test")
	@MapKeyColumn(name = "key")
	@JoinColumn(name = "auth_config_id")
	private Map<String, String> authPropertyMap;
	private Boolean enabled;
	public AuthConfig() {
		authPropertyMap = new HashMap<String, String>() {

			private static final long serialVersionUID = 1L;

		};
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

	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
}
