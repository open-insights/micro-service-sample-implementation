package com.oi.hermes.customer.exception.type;

public class InvalidCredentialsException extends RuntimeException {
	private static final long serialVersionUID = -5099945288390775983L;
	String message;

	public InvalidCredentialsException(Throwable th, String message) {
		super(th);
		this.message=message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
