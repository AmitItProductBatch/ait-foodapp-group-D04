package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.DeliveryRuleService;
import com.ait.app.model.DeliveryRules;
import com.ait.app.requestBody.DeliveryRuleDto;

@RestController
@RequestMapping("/api/prices")
public class DeliveryRuleController {
	
	@Autowired
	DeliveryRuleService ruleservice;
	
	@PutMapping("deliveryrules")
	public ResponseEntity updateDeliveryRule(@RequestBody DeliveryRuleDto dto) {
		
		return  ruleservice.updateDeliveryRules(dto);
		
	
	}
	
	

}
