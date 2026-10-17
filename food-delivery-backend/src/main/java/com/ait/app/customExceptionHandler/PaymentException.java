package com.ait.app.customExceptionHandler;

import org.springframework.http.HttpStatus;

public class PaymentException extends RuntimeException{
	
	public String msg;
	public HttpStatus statusCode;
	public PaymentException(String msg, HttpStatus statusCode) {
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
