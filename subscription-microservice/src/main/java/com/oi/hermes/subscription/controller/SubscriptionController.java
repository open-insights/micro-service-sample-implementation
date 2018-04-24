package com.oi.hermes.subscription.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oi.hermes.subscription.entity.Subscription;
import com.oi.hermes.subscription.service.SubscriptionService;

@BasePathAwareController
public class SubscriptionController {
	public static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionController.class);

	@Autowired
	SubscriptionService subscriptionService;

	@GetMapping("subscriptions/{subscriptionId}")
	@ResponseBody
	Subscription getSubscription(@PathVariable("subscriptionId") String subscriptionId) {
		return subscriptionService.getSubscription(subscriptionId);

	}

}
