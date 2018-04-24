package com.oi.hermes.customer.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import com.oi.hermes.customer.dao.CustomerRepository;
import com.oi.hermes.customer.entity.Customer;

@Service
public class CustomerService {
	public static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
	@Autowired
	private CustomerRepository customerRepository;

	public Customer getCustomer(Long customerId) {
		Customer customer = customerRepository.findAllByCustomerId(customerId);
		if (customer == null) {
			throw new ResourceNotFoundException("Customer " + customerId + " not found",
					new Exception("Customer " + customerId + " not found"));
		}
		return customer;
	}

}
