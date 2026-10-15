package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.requestBody.DeliveryPartnerAvailabilityDto;
import com.ait.app.requestBody.DeliveryPartnerRegisterDto;

public interface DeliveryPartnerService {

	ResponseEntity register(DeliveryPartnerRegisterDto dto);

	ResponseEntity updateAvailability(DeliveryPartnerAvailabilityDto dto);
	
    ResponseEntity<String> acceptOrder(int orderId, Integer deliveryPartnerId);
	
	ResponseEntity<String> rejectOrder(int orderId, Integer deliveryPartnerId);
	
	ResponseEntity updateOrderStatus(int orderId,String status);
}
