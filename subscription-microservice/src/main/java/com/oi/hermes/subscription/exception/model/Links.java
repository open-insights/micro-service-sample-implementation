package com.oi.hermes.subscription.exception.model;

import java.io.Serializable;

public class Links implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String self;

	public Links(String self) {
		super();
		this.self = self;
	}

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}
	
}