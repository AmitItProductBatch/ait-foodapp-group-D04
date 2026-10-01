package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.RatingService;
import com.ait.app.requestBody.RatingDto;

@RestController
@RequestMapping("/api/feedback")
public class RatingController {

	@Autowired
	private RatingService ratingService;

	@PostMapping("/ratings")
	public ResponseEntity<String> rateRestaurant(@RequestBody RatingDto dto) {

		return ratingService.rateRestaurant(dto);
	}

}