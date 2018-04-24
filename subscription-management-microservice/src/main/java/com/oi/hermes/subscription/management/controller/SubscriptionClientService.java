package com.oi.hermes.subscription.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oi.hermes.subscription.management.entity.Subscription;


@Configuration
@EnableFeignClients
@EnableDiscoveryClient
public class SubscriptionClientService {

	@Autowired
	private SubscriptionService subscriptionService;

	@FeignClient(name = "subscription-microservice", path = "/api/subscriptions")
	interface SubscriptionService {

		
		@RequestMapping(method = RequestMethod.GET, path = "/{subscriptionId}", consumes = "application/json", produces = "application/hal+json")
		public ResponseEntity<Resource<Subscription>> getSubscription(@PathVariable("subscriptionId") String subscriptionId, @RequestHeader("Authorization") String token);
	}

	public ResponseEntity<Resource<Subscription>> getSubscription(String subscriptionId,String token) {
		return subscriptionService.getSubscription(subscriptionId,token);
	}

}