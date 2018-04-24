package com.oi.hermes.subscription.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.oi.hermes.subscription.management.exception.CustomRestClientErrorDecoder;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SubscriptionManagementApplication {

	public static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionManagementApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionManagementApplication.class, args);
	}
}
