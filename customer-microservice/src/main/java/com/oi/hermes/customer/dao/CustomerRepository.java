package com.oi.hermes.customer.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oi.hermes.customer.entity.Customer;

@RepositoryRestResource
public interface CustomerRepository extends
		JpaRepository<Customer, Long> {
	List<Customer> findByName(@Param("name") String name);
	Customer findAllByCustomerId(Long customerId);

}