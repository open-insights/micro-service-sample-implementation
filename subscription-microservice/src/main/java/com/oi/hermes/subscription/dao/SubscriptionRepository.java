package com.oi.hermes.subscription.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.oi.hermes.subscription.entity.Subscription;

@RepositoryRestResource
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	Subscription findBySubscriptionId(String subscriptionId);
}