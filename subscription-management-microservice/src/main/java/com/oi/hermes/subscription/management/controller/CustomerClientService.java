package com.oi.hermes.subscription.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oi.hermes.subscription.management.dao.Customer;


@Configuration
@EnableFeignClients
@EnableDiscoveryClient
public class CustomerClientService {

	@Autowired
	private CustomerService customerService;

	@FeignClient(name = "customer-microservice", path = "/api/customers")
	interface CustomerService {

		@RequestMapping(method = RequestMethod.GET, path = "/{customerId}", consumes = "application/json", produces = "application/hal+json")
		@ResponseBody
		ResponseEntity<Resource<Customer>> getCustomer(@PathVariable("customerId") Long customerId, @RequestHeader("Authorization") String token);
	}
	public ResponseEntity<Resource<Customer>> getCustomer(Long customerId, String token) {
		return customerService.getCustomer(customerId,token);
	}

}