package com.ait.app.Service;

import com.ait.app.requestBody.DeliveryFeeRequestDto;
import com.ait.app.response.DeliveryFeeResponseDto;

public interface DeliveryFeeService {
	
	
	public DeliveryFeeResponseDto calculateDeliveryFeeByDistance(DeliveryFeeRequestDto reqDto);


}
