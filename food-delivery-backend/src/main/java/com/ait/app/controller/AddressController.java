package com.ait.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.ServiceImpl.AddressServiceImpl;
import com.ait.app.model.Address;
import com.ait.app.requestBody.AddressDto;



@RestController
public class AddressController {
	
	@Autowired
	AddressServiceImpl addressServiceImpl;

	@PostMapping("add/address/{userId}")
	public ResponseEntity saveAddress(@PathVariable Integer userId, @RequestBody AddressDto addDto) {

		addressServiceImpl.saveAddress(userId, addDto);

		return new ResponseEntity("Done",HttpStatus.CREATED);
	}

	@GetMapping("get/Alladdress/{userId}")
	public ResponseEntity<List<Address>> getAllAddress(@PathVariable Integer userId) {

		return new ResponseEntity<>(addressServiceImpl.getAllAddressBtUserId(userId), HttpStatus.OK);

}

}
