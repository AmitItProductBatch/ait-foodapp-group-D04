package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.requestBody.PaymentDto;
import com.ait.app.requestBody.PaymentVerifyDto;

public interface PaymentService {
	
	ResponseEntity createPayment(PaymentDto dto);
	
	ResponseEntity verifyPayment(PaymentVerifyDto dto);

}
