package com.ait.app.Service;

import com.ait.app.model.Feedback;
import com.ait.app.requestBody.FeedbackDto;

public interface FeedbackService {

	Feedback createFeedback(FeedbackDto feedbackDto);

}