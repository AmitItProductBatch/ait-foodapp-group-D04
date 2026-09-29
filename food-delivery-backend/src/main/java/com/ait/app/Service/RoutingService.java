package com.ait.app.Service;

public interface RoutingService {
	
	 double getDistance(double restaurantLatitude,
             double restaurantLongitude,
             double addressLatitude,
             double addressLongitude);

}
