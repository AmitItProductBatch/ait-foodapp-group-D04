package com.ait.app.globalExceptionHandler;

import java.util.HashMap;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.ait.app.customExceptionHandler.AddressException;
import com.ait.app.customExceptionHandler.CartException;
import com.ait.app.customExceptionHandler.CartItemServiceException;
import com.ait.app.customExceptionHandler.PriceCalculationException;
import com.ait.app.customExceptionHandler.RestaurantException;
import com.ait.app.customExceptionHandler.RoleException;
import com.ait.app.customExceptionHandler.UserException;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(UserException.class)
	public ResponseEntity handleUSerServiceException(UserException e) {

		Map error = new HashMap<>();
		error.put("errorMsg", e.getMsg());

		return ResponseEntity.status(e.getStatusCode()).body(error);
	}

	@ExceptionHandler(AddressException.class)
	public ResponseEntity<String> handleAddressException(AddressException a) {

		return new ResponseEntity(a.getMessage(), a.getHttpStatus());

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity handleException(Exception e) {

		return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(RestaurantException.class)
	public ResponseEntity handleRestaurantServiceException(RestaurantException e) {

		Map error = new HashMap<>();
		error.put("error", e.getMsg());

		return ResponseEntity.status(e.getStatusCode()).body(error);
	}

	@ExceptionHandler(CartException.class)
	public ResponseEntity handleCartException(CartException e) {

		return new ResponseEntity(e.getMessage(), e.getHttpStatus());
	}

	@ExceptionHandler(CartItemServiceException.class)
	public ResponseEntity<String> handleCartItemServiceException(CartItemServiceException cartItemServiceException) {

		return new ResponseEntity<>(cartItemServiceException.getMessage(), cartItemServiceException.getHttpStatus());
	}

	@ExceptionHandler(RoleException.class)
	public ResponseEntity handleRoleException(RoleException roleException) {
		return new ResponseEntity(roleException.getMessage(), roleException.getHttpStatus());

	}

	@ExceptionHandler(PriceCalculationException.class)
	public ResponseEntity handlePriceCalculationException(PriceCalculationException p) {

		return new ResponseEntity(p.getMsg(), p.getStatusCode());

	}

}
