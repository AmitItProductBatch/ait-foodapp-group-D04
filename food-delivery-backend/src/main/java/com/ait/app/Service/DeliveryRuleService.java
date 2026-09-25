package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.requestBody.DeliveryRuleDto;

public interface DeliveryRuleService {
	
	ResponseEntity updateDeliveryRules(DeliveryRuleDto dto);

}
