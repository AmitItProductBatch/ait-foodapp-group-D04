package com.ait.app.globalExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ait.app.customExceptionHandler.AddressException;
@ControllerAdvice
public class GlobalException {

	   @ExceptionHandler(AddressException.class)
	    public ResponseEntity<String> handleAddressException(AddressException a) {

	        return new ResponseEntity( a.getMessage(),a.getHttpStatus());
	        
	    }

	   @ExceptionHandler(Exception.class)
	    public ResponseEntity handleException(Exception e) {

	        return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
	        
	   }
}
