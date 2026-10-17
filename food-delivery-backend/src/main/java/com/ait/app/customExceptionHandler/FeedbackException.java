package com.ait.app.customExceptionHandler;

import java.io.ObjectInputFilter.Status;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class FeedbackException extends RuntimeException{
	
	private String msg;
	private HttpStatus status;
	
	
	public FeedbackException(String msg, HttpStatus status) {
		super();
		this.msg = msg;
		this.status = status;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public HttpStatusCode getStatus() {
		return status;
	}


	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	
	

}
