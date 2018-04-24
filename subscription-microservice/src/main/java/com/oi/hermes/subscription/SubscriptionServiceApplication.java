package com.oi.hermes.subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SubscriptionServiceApplication {

	public static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionServiceApplication.class, args);
	}

}
