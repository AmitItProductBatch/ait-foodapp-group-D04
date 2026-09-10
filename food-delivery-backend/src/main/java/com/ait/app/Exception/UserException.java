package com.ait.app.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;



public class UserException extends RuntimeException {
	
	public String msg;
	public HttpStatus statusCode;

	
	public UserException(String msg,HttpStatus statusCode) {
		
		this.msg=msg;
		this.statusCode=statusCode;
	}
	
	 public String getMsg() {
	        return msg;
	    }

	    public HttpStatus getStatusCode() {
	        return statusCode;
	    }
}
