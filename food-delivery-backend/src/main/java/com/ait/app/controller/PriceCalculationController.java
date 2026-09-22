package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.PriceCalculationService;
import com.ait.app.requestBody.PriceCalculationRequestDto;
import com.ait.app.response.PriceCalculationResponse;

@RestController
@RequestMapping("/api/prices")
public class PriceCalculationController {
	
	@Autowired
	PriceCalculationService priceCalculationService;
	
	@PostMapping("/calculate")
	public ResponseEntity<PriceCalculationResponse> calculatePrice(@RequestBody PriceCalculationRequestDto priceDto) {
		
		return priceCalculationService.calculatePrice(priceDto);
		
	}

}
