package com.oi.hermes.authentication.gateway.security.dao;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oi.hermes.authentication.gateway.security.entity.AuthConfig;

@Repository
public interface AuthenticationConfigRepository extends JpaRepository<AuthConfig,Long>{
	AuthConfig save(@Valid AuthConfig authConfig);
	public List<AuthConfig> findAll();
	public Iterable<AuthConfig> findByEnabled(Boolean enabled);
}
