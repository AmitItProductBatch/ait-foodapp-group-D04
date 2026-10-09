package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.PaymentService;
import com.ait.app.requestBody.PaymentDto;
import com.ait.app.requestBody.PaymentVerifyDto;

@RestController
public class PaymetController {
	
	@Autowired
	PaymentService paymentService;
	
	@PostMapping("create-payment")
	ResponseEntity createPayment(@RequestBody PaymentDto dto) {
		System.out.println("create-payment");
		return paymentService.createPayment(dto);
		
	}
	
	@PostMapping("verify-payment")
	ResponseEntity verifyPayment(@RequestBody PaymentVerifyDto dto) {
		
		return paymentService.verifyPayment(dto);
	}

}
