package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.DeliveryFeeService;
import com.ait.app.Service.OrderTotalService;
import com.ait.app.Service.PriceCalculationService;
import com.ait.app.requestBody.DeliveryFeeRequestDto;
import com.ait.app.requestBody.OrderTotalRequestDto;
import com.ait.app.requestBody.PriceCalculationRequestDto;
import com.ait.app.response.DeliveryFeeResponseDto;
import com.ait.app.response.OrderTotalResponseDto;
import com.ait.app.response.PriceCalculationResponse;

@RestController
@RequestMapping("/api/prices")
public class PriceCalculationController {
	
	@Autowired
	PriceCalculationService priceCalculationService;
	
	@Autowired
	DeliveryFeeService deliveryFeeService;
	
	@Autowired
	OrderTotalService orderTotalService; 
	
	@PostMapping("/sub-total")
	public ResponseEntity<PriceCalculationResponse> calculatePrice(@RequestBody PriceCalculationRequestDto priceDto) {
		
		return priceCalculationService.calculatePrice(priceDto);
		
	}
	
	@PostMapping("/delivery-fee")
	public ResponseEntity<DeliveryFeeResponseDto> calculateDeliveryFeeByDistance(@RequestBody DeliveryFeeRequestDto reqDto) {
		
		DeliveryFeeResponseDto response = deliveryFeeService.calculateDeliveryFeeByDistance(reqDto);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}
	
	@PostMapping("/order-total")
	public ResponseEntity<OrderTotalResponseDto> calculateOrdertotal(@RequestBody OrderTotalRequestDto totalRequestDto){
		
		OrderTotalResponseDto response =orderTotalService.calculateOrdertotal(totalRequestDto);
		
		return new ResponseEntity<>(response,HttpStatus.OK);
		
	}

}
