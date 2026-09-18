package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.RestaurantService;
import com.ait.app.requestBody.RestaurantDto;

@RequestMapping("/api/restaurant")
@RestController
public class RestaurantController {

	@Autowired
	RestaurantService restaurantService;

	@PostMapping("/saveRestaurant")
	ResponseEntity saveRestaurant(@RequestBody RestaurantDto dto) {

		return restaurantService.addRestaurant(dto);
	}

	@GetMapping("/getRestaurant")
	public ResponseEntity<Page<RestaurantDto>> getRestaurants(

			@RequestParam(required = false) Double rating,

			@RequestParam(required = false) String location,

			@RequestParam(required = false) String cuisine,

			Pageable pageable) {

		Page<RestaurantDto> page = restaurantService.getRestaurants(rating, location, cuisine, pageable);

		return ResponseEntity.ok(page);
	}

}
