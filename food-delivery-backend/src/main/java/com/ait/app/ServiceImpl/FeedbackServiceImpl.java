package com.ait.app.ServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.Service.FeedbackService;
import com.ait.app.customExceptionHandler.FeedbackException;
import com.ait.app.model.Feedback;
import com.ait.app.repository.FeedbackRepository;
import com.ait.app.requestBody.FeedbackDto;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackRepository feedbackRepository;

	@Override
	public Feedback createFeedback(FeedbackDto feedbackDto) {

		if (feedbackDto.getRating() < 1 || feedbackDto.getRating() > 5) {
			throw new RuntimeException("Rating must be between 1 and 5");
		}

		if (feedbackRepository.existsByUserIdAndOrderId(feedbackDto.getUserId(), feedbackDto.getOrderId())) {

			throw new RuntimeException("Feedback already exists for this order");
		}

		Feedback feedback = new Feedback();

		feedback.setUserId(feedbackDto.getUserId());
		feedback.setRestaurantId(feedbackDto.getRestaurantId());
		feedback.setOrderId(feedbackDto.getOrderId());
		feedback.setRating(feedbackDto.getRating());
		feedback.setComment(feedbackDto.getComment());

		return feedbackRepository.save(feedback);
	}

	@Override
	public void deleteFeedBack(int feedbackId) {
		Optional<Feedback> optional = feedbackRepository.findById(feedbackId);
		if (optional.isEmpty()) {
			throw new FeedbackException("Feedback not found for id: " + feedbackId,HttpStatus.NOT_FOUND);
		}
		Feedback feedback = optional.get();

		feedbackRepository.deleteById(feedback.getId());
	}
}