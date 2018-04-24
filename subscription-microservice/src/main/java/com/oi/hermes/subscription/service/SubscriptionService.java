package com.oi.hermes.subscription.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.oi.hermes.subscription.dao.SubscriptionRepository;
import com.oi.hermes.subscription.entity.Subscription;

@Service
public class SubscriptionService {
	public static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionService.class);
	@Autowired
	private SubscriptionRepository subscriptionRepository;

	public Subscription getSubscription(String subscriptionId) {
		return subscriptionRepository.findBySubscriptionId(subscriptionId);
	}

}
