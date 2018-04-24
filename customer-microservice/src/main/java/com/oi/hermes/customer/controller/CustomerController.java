package com.oi.hermes.customer.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oi.hermes.customer.entity.Customer;
import com.oi.hermes.customer.service.CustomerService;

@BasePathAwareController
public class CustomerController {
	public static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	CustomerService customerService;

	@GetMapping("customers/{customerId}")

	@ResponseBody
	Customer getSubscription(@PathVariable("customerId") Long customerId) {
		Customer cus = customerService.getCustomer(customerId);
		return cus;
	}

}
