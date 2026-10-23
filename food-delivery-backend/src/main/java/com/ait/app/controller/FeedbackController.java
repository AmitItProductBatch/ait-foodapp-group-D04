package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.FeedbackService;
import com.ait.app.model.Feedback;
import com.ait.app.requestBody.FeedbackDto;
import com.ait.app.requestBody.FeedbackUpdateDto;
import com.ait.app.response.FeedbackResponse;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;

	@PostMapping
	public ResponseEntity<Feedback> createFeedback(@RequestBody FeedbackDto feedbackDto) {

		Feedback feedback = feedbackService.createFeedback(feedbackDto);

		return ResponseEntity.ok(feedback);
	}
	
	@GetMapping("/{restaurantId}")
	public ResponseEntity<Page<FeedbackResponse>> getRestaurantFeedback(@PathVariable int restaurantId,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<FeedbackResponse> feedbackPage = feedbackService.getFeedbackByRestaurantId(restaurantId, page, size);
		return new ResponseEntity(feedbackPage, HttpStatus.OK);
	}
	@PutMapping("/{feedbackId}")
	public ResponseEntity updateFeedback(@PathVariable int feedbackId, @RequestBody FeedbackUpdateDto updateDto) {
		
		
		return feedbackService.updateFeedback(feedbackId, updateDto);
	}
}
