package com.ait.app.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalException {
	@ExceptionHandler(UserServicException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserServicException e) {

		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {

		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
