package com.oi.hermes.customer.exception.model;

public class ApiValidationError {
	public ApiValidationError(String object, String message) {
		this.message = message;
	}

	private String field;
	private Object rejectedValue;
	private String message;

	public ApiValidationError(String field, Object rejectedValue, String message) {
		super();
		this.field = field;
		this.rejectedValue = rejectedValue;
		this.message = message;
	}

}