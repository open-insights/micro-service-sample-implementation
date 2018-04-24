package com.oi.hermes.subscription.management.exception.type;

import feign.FeignException;

public class CustomFeignClientException extends FeignException {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	int status;

	protected CustomFeignClientException(String message, Throwable cause) {
		super(message, cause);
	}

	protected CustomFeignClientException(String message) {
		super(message);
	}

	public CustomFeignClientException(int status, String message) {
		super(message);
		this.status = status;
	}

	public int status() {
		return this.status;
	}
}
