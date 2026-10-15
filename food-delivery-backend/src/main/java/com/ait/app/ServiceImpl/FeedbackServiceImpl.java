package com.ait.app.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.Service.FeedbackService;
import com.ait.app.model.Feedback;
import com.ait.app.model.Order;
import com.ait.app.repository.FeedbackRepository;
import com.ait.app.repository.OrderRepository;
import com.ait.app.repository.RestaurantRepo;
import com.ait.app.requestBody.FeedbackDto;
import com.ait.app.customExceptionHandler.OrderException;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackRepository feedbackRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestaurantRepo restaurantRepo;

	@Override
	public Feedback createFeedback(FeedbackDto feedbackDto) {

		if (feedbackDto.getRating() < 1 || feedbackDto.getRating() > 5) {
			throw new OrderException("Rating must be between 1 and 5", HttpStatus.BAD_REQUEST);
		}

		if (feedbackDto.getComment() == null || feedbackDto.getComment().trim().isEmpty()) {
			throw new OrderException("Comment cannot be empty", HttpStatus.BAD_REQUEST);
		}

		Order order = orderRepository.findById(feedbackDto.getOrderId())
				.orElseThrow(() -> new OrderException("Order not found", HttpStatus.NOT_FOUND));

		if (order.getUserId() != feedbackDto.getUserId()) {
			throw new OrderException("This order does not belong to the specified user", HttpStatus.FORBIDDEN);
		}

		if (order.getRestaurantId() != feedbackDto.getRestaurantId()) {
			throw new OrderException("Restaurant does not match the order", HttpStatus.BAD_REQUEST);
		}

		if (!restaurantRepo.existsById(feedbackDto.getRestaurantId())) {
			throw new OrderException("Restaurant not found", HttpStatus.NOT_FOUND);
		}

		if (feedbackRepository.existsByUserIdAndOrderId(feedbackDto.getUserId(), feedbackDto.getOrderId())) {
			throw new OrderException("Feedback already submitted for this order", HttpStatus.CONFLICT);
		}

		Feedback feedback = new Feedback();
		feedback.setUserId(feedbackDto.getUserId());
		feedback.setRestaurantId(feedbackDto.getRestaurantId());
		feedback.setOrderId(feedbackDto.getOrderId());
		feedback.setRating(feedbackDto.getRating());
		feedback.setComment(feedbackDto.getComment().trim());

		return feedbackRepository.save(feedback);
	}
}
