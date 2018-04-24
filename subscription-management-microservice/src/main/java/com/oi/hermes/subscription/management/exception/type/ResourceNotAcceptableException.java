package com.oi.hermes.subscription.management.exception.type;

public class ResourceNotAcceptableException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;

	public ResourceNotAcceptableException(String message) {
		super(message);
		this.message = message;
	}

}
