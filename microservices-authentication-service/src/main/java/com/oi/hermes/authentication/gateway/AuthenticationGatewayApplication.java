package com.oi.hermes.authentication.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableDiscoveryClient
@SpringBootApplication
@EnableZuulProxy
public class AuthenticationGatewayApplication {

	public static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationGatewayApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationGatewayApplication.class, args);
	}

}
