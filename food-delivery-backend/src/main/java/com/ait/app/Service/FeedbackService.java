package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.model.Feedback;
import com.ait.app.requestBody.FeedbackDto;
import com.ait.app.requestBody.FeedbackUpdateDto;
import com.ait.app.response.FeedbackResponse;

public interface FeedbackService {

    Feedback createFeedback(FeedbackDto feedbackDto);

    ResponseEntity<FeedbackResponse> updateFeedback(int feedbackId, FeedbackUpdateDto updateDto);

}