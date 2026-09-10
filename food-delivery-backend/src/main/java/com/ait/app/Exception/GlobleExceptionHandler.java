package com.ait.app.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobleExceptionHandler {

	@ExceptionHandler(UserException.class)
	public ResponseEntity handleUSerServiceException(UserException e) {
		
		Map error=new HashMap<>();
		error.put("errorMsg", e.getMsg());
		
		return ResponseEntity.status(e.getStatusCode()).body(error);
	}
}
