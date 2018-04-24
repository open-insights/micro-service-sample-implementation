package com.oi.hermes.subscription.exception.type;

public class GeneralException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8630136942316861014L;
	String message;

	public GeneralException(Exception ex, String message) {
		super(ex);
		this.message = message;
	}

}
