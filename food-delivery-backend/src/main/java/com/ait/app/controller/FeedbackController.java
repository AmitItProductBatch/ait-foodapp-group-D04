package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.FeedbackService;
import com.ait.app.model.Feedback;
import com.ait.app.requestBody.FeedbackDto;

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
}