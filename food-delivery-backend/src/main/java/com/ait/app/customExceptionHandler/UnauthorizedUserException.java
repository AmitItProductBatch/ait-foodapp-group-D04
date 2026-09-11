package com.ait.app.customExceptionHandler;

public class UnauthorizedUserException extends RuntimeException {
	public UnauthorizedUserException(String message) {
		super(message);
	}
}
