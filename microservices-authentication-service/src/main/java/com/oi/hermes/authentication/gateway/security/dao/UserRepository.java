package com.oi.hermes.authentication.gateway.security.dao;

import java.util.stream.Stream;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import com.oi.hermes.authentication.gateway.security.entity.User;

@RepositoryRestController
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    @Query("select c from User c")
	Stream<User> findAllUsers();
}
