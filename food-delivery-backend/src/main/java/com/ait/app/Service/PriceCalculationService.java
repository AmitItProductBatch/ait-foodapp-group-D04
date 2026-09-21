package com.ait.app.Service;

import org.springframework.http.ResponseEntity;


import com.ait.app.requestBody.PriceCalculationRequestDto;
import com.ait.app.response.PriceCalculationResponse;

public interface PriceCalculationService {
	
ResponseEntity<PriceCalculationResponse> calculatePrice(PriceCalculationRequestDto priceDto);

}
