package com.ait.app.controller;

import com.ait.app.Service.DeliveryPartnerService;
import com.ait.app.requestBody.DeliveryPartnerAvailabilityDto;
import com.ait.app.requestBody.DeliveryPartnerRegisterDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery-partners")
public class DeliveryPartnerController {

	@Autowired
	DeliveryPartnerService service;

	@PostMapping("/register")
	public ResponseEntity register(@RequestBody DeliveryPartnerRegisterDto dto) {

		return service.register(dto);
	}

	@PatchMapping("/availability")
	public ResponseEntity updateAvailability(@RequestBody DeliveryPartnerAvailabilityDto dto) {

		return service.updateAvailability(dto);
	}

	@PatchMapping("/{orderId}/{deliveryPartnerId}/accept")
	public ResponseEntity<String> acceptOrder(@PathVariable int orderId, @PathVariable Integer deliveryPartnerId) {

		return service.acceptOrder(orderId, deliveryPartnerId);

	}

	@PatchMapping("/{orderId}/{deliveryPartnerId}/reject")
	public ResponseEntity<String> rejectOrder(@PathVariable int orderId, @PathVariable Integer deliveryPartnerId) {

		return service.rejectOrder(orderId, deliveryPartnerId);
	}

}