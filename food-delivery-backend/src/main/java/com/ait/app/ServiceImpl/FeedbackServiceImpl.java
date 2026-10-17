package com.ait.app.ServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.FeedbackService;
import com.ait.app.customExceptionHandler.FeedbackException;
import com.ait.app.model.Feedback;
import com.ait.app.repository.FeedbackRepository;
import com.ait.app.requestBody.FeedbackDto;
import com.ait.app.requestBody.FeedbackUpdateDto;
import com.ait.app.response.FeedbackResponse;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackRepository feedbackRepository;
	
	private static final org.slf4j.Logger logger =LoggerFactory.getLogger(FeedbackServiceImpl.class);

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
	public ResponseEntity updateFeedback(int feedbackId, FeedbackUpdateDto updateDto) {
		// TODO Auto-generated method stub
		
		Optional<Feedback> feedbackOpt= feedbackRepository.findById(feedbackId);
		
		if(feedbackOpt.isEmpty()) {
			
			throw new FeedbackException("Feedback not found with given "+feedbackId+" feedback Id", HttpStatus.NOT_FOUND);
		}
		
		Feedback feedback=feedbackOpt.get();
		
		long editWindowHours=72;
		
		if(feedback.getCreatedAt().plusHours(editWindowHours).isBefore(LocalDateTime.now())) {
			
			throw new FeedbackException("Feedback editing time window has expired", HttpStatus.FORBIDDEN);
		}
		
		if(updateDto.getRating()<1 || updateDto.getRating()>5) {
			throw new FeedbackException("Rating should be between 1 to 5", HttpStatus.BAD_REQUEST);
		}
		if(updateDto.getComment()==null || updateDto.getComment().trim().isEmpty()) {
			throw new FeedbackException("Comment is required", HttpStatus.BAD_REQUEST);
		}
		try {
		feedback.setRating(updateDto.getRating());
		feedback.setComment(updateDto.getComment());
		
		 Feedback updatedFeedback =
		            feedbackRepository.saveAndFlush(feedback);
		 logger.info("Feedback " +feedbackId+" is Updated ");
		 
		 FeedbackResponse response=new FeedbackResponse();
		    response.setId(updatedFeedback.getId());
		    response.setUserId(updatedFeedback.getUserId());
		    response.setRestaurantId(updatedFeedback.getRestaurantId());
		    response.setOrderId(updatedFeedback.getOrderId());
		    response.setRating(updatedFeedback.getRating());
		    response.setComment(updatedFeedback.getComment());
		    response.setCreatedAt(updatedFeedback.getCreatedAt());
		    response.setUpdatedAt(updatedFeedback.getUpdatedAt());
	
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
		}catch(Exception e) {
			throw new RuntimeException("Exception Occured while updating Feedback "+e);
		}
	}
}