package com.ait.app.customExceptionHandler;

import org.springframework.http.HttpStatus;

public class DeliveryPartnerNotFoundException extends RuntimeException{
	private String errorMessage;
	private HttpStatus httpStatus;
	public DeliveryPartnerNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DeliveryPartnerNotFoundException(String errorMessage, HttpStatus httpStatus) {
		super();
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
