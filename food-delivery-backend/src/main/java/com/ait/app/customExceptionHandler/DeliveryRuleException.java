package com.ait.app.customExceptionHandler;

import org.springframework.http.HttpStatus;

public class DeliveryRuleException extends RuntimeException{
	
	public String msg;
	public HttpStatus statusCode;
	    
	    public DeliveryRuleException(String msg, HttpStatus statusCode) {
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
