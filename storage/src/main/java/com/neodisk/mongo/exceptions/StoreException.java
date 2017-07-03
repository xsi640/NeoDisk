package com.neodisk.mongo.exceptions;

public class StoreException extends Exception {

	private static final long serialVersionUID = 1L;

	public StoreException(Exception e) {
		super(e);
	}

	public StoreException(String message) {
		super(message);
	}
}
