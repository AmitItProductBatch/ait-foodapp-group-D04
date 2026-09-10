package com.ait.app.customExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserException extends RuntimeException{

	public String msg;
	public HttpStatus statusCode;
	
	
	public UserException(String msg, HttpStatus statusCode) {
		super();
		this.msg = msg;
		this.statusCode = statusCode;
	}


	public String getMsg() {
		return msg;
	}


	public HttpStatus getStatusCode() {
		return statusCode;
	}
	
	
}
