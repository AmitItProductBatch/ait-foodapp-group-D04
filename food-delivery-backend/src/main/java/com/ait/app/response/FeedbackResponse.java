package com.ait.app.response;

import java.time.LocalDateTime;

public class FeedbackResponse {
	
	    private int id;
	    private int userId;
	    private int restaurantId;
	    private int orderId;
	    private int rating;
	    private String comment;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public int getRestaurantId() {
			return restaurantId;
		}
		public void setRestaurantId(int restaurantId) {
			this.restaurantId = restaurantId;
		}
		public int getOrderId() {
			return orderId;
		}
		public void setOrderId(int orderId) {
			this.orderId = orderId;
		}
		public int getRating() {
			return rating;
		}
		public void setRating(int rating) {
			this.rating = rating;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}
		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}
		public FeedbackResponse(int id, int userId, int restaurantId, int orderId, int rating, String comment,
				LocalDateTime createdAt, LocalDateTime updatedAt) {
			super();
			this.id = id;
			this.userId = userId;
			this.restaurantId = restaurantId;
			this.orderId = orderId;
			this.rating = rating;
			this.comment = comment;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
		}
		public FeedbackResponse() {
			super();
			// TODO Auto-generated constructor stub
		}

	    
}
