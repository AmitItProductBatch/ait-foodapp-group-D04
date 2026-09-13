package com.ait.app.globalExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.ait.app.customExceptionHandler.AddressException;
import com.ait.app.customExceptionHandler.RestaurantException;
import com.ait.app.customExceptionHandler.UserException;
import com.ait.app.customExceptionHandler.UserNotFoundException;

@ControllerAdvice
public class GlobalException {
	
		@ExceptionHandler(UserException.class)
		public ResponseEntity handleUSerServiceException(UserException e) {
				
				Map error=new HashMap<>();
				error.put("errorMsg", e.getMsg());
				
				return ResponseEntity.status(e.getStatusCode()).body(error);
		}
		
		
	   @ExceptionHandler(AddressException.class)
	    public ResponseEntity<String> handleAddressException(AddressException a) {

	        return new ResponseEntity( a.getMessage(),a.getHttpStatus());
	        
	    }

	   @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<Map<String, String>> handleValidationException(
	            MethodArgumentNotValidException e) {

	        Map<String, String> errors = new HashMap<>();

	        e.getBindingResult().getFieldErrors()
	                .forEach(error -> errors.put(
	                        error.getField(),
	                        error.getDefaultMessage()
	                ));

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	    }

	   @ExceptionHandler(Exception.class)
	    public ResponseEntity handleException(Exception e) {

	        return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
	        
	   }
	   
	   @ExceptionHandler(RestaurantException.class)
		public ResponseEntity handleRestaurantServiceException(RestaurantException e) {
			
			Map error = new HashMap<>();
			error.put("error", e.getMsg());
			
			return ResponseEntity.status(e.getStatusCode()).body(error);
		}
}