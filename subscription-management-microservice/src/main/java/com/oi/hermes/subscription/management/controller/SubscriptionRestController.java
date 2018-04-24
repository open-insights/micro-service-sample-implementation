package com.oi.hermes.subscription.management.controller;

import java.util.Arrays;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oi.hermes.subscription.management.dao.Customer;
import com.oi.hermes.subscription.management.dao.CustomerSubscriptionRepository;
import com.oi.hermes.subscription.management.entity.CustomerSubscription;
import com.oi.hermes.subscription.management.entity.Subscription;

import feign.FeignException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@BasePathAwareController
public class SubscriptionRestController {
	public static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionRestController.class);
	@Autowired
	private CustomerClientService customerService;
	@Autowired
	SubscriptionClientService subscriptionClientService;
	@Autowired
	CustomerSubscriptionRepository customerSubscriptionRepository;
	@Autowired
	ObjectMapper objectMapper;
	
	@Inject
	private LocalValidatorFactoryBean validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(validator);
	}

	@PostMapping("customerSubscriptions/{customerId}/{subscriptionId}")
	@ResponseBody
	public ResponseEntity<Resource<CustomerSubscription>> subscribe(@PathVariable("customerId") Long customerId,
			@PathVariable("subscriptionId") String subscriptionId,
			@RequestHeader("Authorization") String authorization) {
		CustomerSubscription customerSubscription = new CustomerSubscription();
		ResponseEntity<Resource<Customer>> customer = null;
		customer = customerService.getCustomer(customerId, authorization);
		Customer customerResponse = customer.getBody().getContent();
		ResponseEntity<Resource<Subscription>> subscription = subscriptionClientService.getSubscription(subscriptionId,
				authorization);
		Subscription subscriptionResponse = subscription.getBody().getContent();
		customerSubscription.setFirstname(customerResponse.getFirstname());
		customerSubscription.setCustomerId(customerResponse.getCustomerId());
		customerSubscription.setEmail(customerResponse.getEmail());
		customerSubscription.setCity(customerResponse.getCity());
		customerSubscription.setSubscription(Arrays.asList(subscriptionResponse));
		Resource<CustomerSubscription> resources = new Resource<CustomerSubscription>(
				customerSubscriptionRepository.save(customerSubscription));
		resources.add(
				linkTo(methodOn(SubscriptionRestController.class).subscribe(customerId, subscriptionId, authorization))
						.withSelfRel());
		return ResponseEntity.ok(resources);
	}

	@GetMapping("customerSubscriptions/{customerId}")
	@ResponseBody
	public ResponseEntity<Resource<CustomerSubscription>> subscribtionList(
			@PathVariable("customerId") String customerId, @RequestHeader("Authorization") String authorization) {
		Resource<CustomerSubscription> resources = new Resource<CustomerSubscription>(
				customerSubscriptionRepository.findByCustomerId(customerId));
		resources.add(linkTo(methodOn(SubscriptionRestController.class).subscribtionList(customerId, authorization))
				.withSelfRel());
		return ResponseEntity.ok(resources);

	}

	@PutMapping("customerSubscriptions/{customerId}/{subscriptionId}")
	@ResponseBody
	public ResponseEntity<Resource<CustomerSubscription>> unsubscribe(@PathVariable("customerId") String customerId,
			@PathVariable("subscriptionId") String subscriptionId) {
		CustomerSubscription customerSubscription = customerSubscriptionRepository
				.findByCustomerIdAndSubscriptionId(customerId, subscriptionId);
		LOGGER.info("" + customerSubscription);
		Resource<CustomerSubscription> resources = new Resource<CustomerSubscription>(customerSubscription);

		resources.add(linkTo(methodOn(SubscriptionRestController.class).unsubscribe(customerId, subscriptionId))
				.withSelfRel());
		return ResponseEntity.ok(resources);

	}
}
