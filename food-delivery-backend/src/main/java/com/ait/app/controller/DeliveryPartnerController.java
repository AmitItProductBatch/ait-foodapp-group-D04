package com.ait.app.controller;

import com.ait.app.Service.DeliveryPartnerService;
import com.ait.app.requestBody.DeliveryPartnerAvailabilityDto;
import com.ait.app.requestBody.DeliveryPartnerRegisterDto;
import com.ait.app.requestBody.DeliveryPartnerRequestDto;
import com.ait.app.response.DeliveryPartnerResponseDto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	@GetMapping("/{deliveryPartnerId}")
	ResponseEntity getDeliveryPartner(@PathVariable int deliveryPartnerId) {

		DeliveryPartnerResponseDto deliveryPartnerResponseDto = service
				.getDeliveryPartner(deliveryPartnerId);

		return new ResponseEntity(deliveryPartnerResponseDto, HttpStatus.OK);

	}
    @PutMapping("/{deliveryPartnerId}")
	ResponseEntity updateDeliveryPartner(@PathVariable int deliveryPartnerId,@RequestBody DeliveryPartnerRequestDto deliveryPartnerRequestDto) {

		DeliveryPartnerResponseDto deliveryPartnerResponseDto = service.updateDeliveryPartner(deliveryPartnerId, deliveryPartnerRequestDto);

		return new ResponseEntity(deliveryPartnerResponseDto, HttpStatus.OK);
	}
    @DeleteMapping("/{deliveryPartnerId}")
	ResponseEntity deleteDeliveryPartner(@PathVariable int deliveryPartnerId) {

		service.deleteDeliveryPartner(deliveryPartnerId);

		return new ResponseEntity("Delivery partner deleted successfully", HttpStatus.OK);
	}
    @GetMapping("/getAllDeliveryPartners")
	ResponseEntity getAllDeliveryPartners() {

		List<DeliveryPartnerResponseDto> list = service.getAllDeliveryPartner();
		return new ResponseEntity(list, HttpStatus.OK);
	}
	@PatchMapping("/availability")
	public ResponseEntity updateAvailability(@RequestBody DeliveryPartnerAvailabilityDto dto) {

		return service.updateAvailability(dto);
	}
}