package com.ait.app.customExceptionHandler;

import org.springframework.http.HttpStatus;

public class RestaurantException extends RuntimeException{
	
	public String msg;
	public HttpStatus statusCode;


	public RestaurantException(String msg, HttpStatus statusCode) {
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
