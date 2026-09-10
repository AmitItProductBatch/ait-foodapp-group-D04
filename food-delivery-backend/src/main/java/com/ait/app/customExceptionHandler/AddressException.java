package com.ait.app.customExceptionHandler;

import org.springframework.http.HttpStatus;

public class AddressException extends RuntimeException {

	private String message;
	private HttpStatus httpStatus;
	
	
	public AddressException(String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
