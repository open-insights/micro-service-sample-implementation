package com.oi.hermes.subscription.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.oi.hermes.subscription.management.entity.CustomerSubscription;

@RepositoryRestResource
public interface CustomerSubscriptionRepository extends JpaRepository<CustomerSubscription, Long> {

	CustomerSubscription findByCustomerId(String customerId);

	CustomerSubscription save(String subscriptionId);

	CustomerSubscription findByCustomerIdAndSubscriptionId(@Param("customerId") String customerId,
			@Param("subscriptionId") String subscriptionId);
}