package com.ait.app.customExceptionHandler;

import org.springframework.http.HttpStatus;

import org.springframework.http.HttpStatus;

public class CartException extends RuntimeException {

	public String message;
	public HttpStatus httpStatus;

	public CartException(String message, HttpStatus httpStatus) {
		super(message);
		this.message = message;
		this.httpStatus = httpStatus;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
