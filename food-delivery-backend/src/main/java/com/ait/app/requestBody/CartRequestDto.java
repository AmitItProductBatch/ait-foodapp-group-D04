package com.ait.app.requestBody;

import jakarta.validation.constraints.NotNull;

public class CartRequestDto {
	@NotNull(message = "User ID is required")
	private int userId;

        private int restaurantId;

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
}
