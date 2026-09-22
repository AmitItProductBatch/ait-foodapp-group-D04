package com.ait.app.customExceptionHandler;

import org.springframework.http.HttpStatus;

public class PriceCalculationException extends RuntimeException{

	private String msg;
	private HttpStatus statusCode;

	public PriceCalculationException(String msg, HttpStatus statusCode) {
		super(msg);
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
