package com.oi.hermes.authentication.gateway.exception.type;

public class ResourceNotModifiedException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;

	public ResourceNotModifiedException(String message) {
		super(message);
		this.message = message;
	}
	
}
