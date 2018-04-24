package com.oi.hermes.customer.exception.type;

public class EntityAlreadyPresentException extends RuntimeException {
	private static final long serialVersionUID = -5099945288390775983L;
	String message;

	public EntityAlreadyPresentException(String message, Exception ex) {
		super(ex);
	}

	public EntityAlreadyPresentException(Exception ex, String message) {
		super(message);
		this.message = message;
	}

}
