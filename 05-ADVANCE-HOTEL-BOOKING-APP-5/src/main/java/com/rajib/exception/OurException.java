package com.rajib.exception;

@SuppressWarnings("serial")
public class OurException extends RuntimeException {

	public OurException() {
		super();
	}
	
	public OurException(String message) {
		super(message);
	}
}
