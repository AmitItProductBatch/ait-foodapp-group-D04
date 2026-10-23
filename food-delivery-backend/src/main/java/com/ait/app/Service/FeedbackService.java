package com.ait.app.Service;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.ait.app.model.Feedback;
import com.ait.app.requestBody.FeedbackDto;
import com.ait.app.requestBody.FeedbackUpdateDto;
import com.ait.app.response.FeedbackResponse;

public interface FeedbackService {

	Feedback createFeedback(FeedbackDto feedbackDto);

	ResponseEntity updateFeedback(int feedbackId, FeedbackUpdateDto updateDto);
	Page<FeedbackResponse> getFeedbackByRestaurantId(int restaurantId, int page, int size);
}