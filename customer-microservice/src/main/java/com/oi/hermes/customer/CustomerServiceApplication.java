package com.oi.hermes.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class CustomerServiceApplication {

	public static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}


}
