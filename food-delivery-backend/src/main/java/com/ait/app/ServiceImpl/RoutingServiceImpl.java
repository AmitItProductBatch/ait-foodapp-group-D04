package com.ait.app.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ait.app.Service.RoutingService;
import com.ait.app.customExceptionHandler.PriceCalculationException;
@Service
public class RoutingServiceImpl implements RoutingService{

	@Autowired
	RestTemplate restTemplate; 
	
	@Override
	public double getDistance(double restaurantLatitude, double restaurantLongitude, double addressLatitude,
			double addressLongitude) {
		try {
		System.out.println("Restaurant Latitude: " + restaurantLatitude);
		System.out.println("Restaurant Longitude: " + restaurantLongitude);
		System.out.println("Address Latitude: " + addressLatitude);
		System.out.println("Address Longitude: " + addressLongitude);
		
        String url = UriComponentsBuilder
                .fromUriString("https://router.project-osrm.org/route/v1/driving/")
                .path(restaurantLongitude + "," + restaurantLatitude+ ";"
                    + addressLongitude + "," + addressLatitude)
                .queryParam("overview", "false")
                .toUriString();
        System.out.println("OSRM URL: " + url);

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        if (response == null) {
            throw new PriceCalculationException("Route could not be found",HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> routes =(List<Map<String, Object>>) response.get("routes");

        if (routes == null || routes.isEmpty()) {
            throw new PriceCalculationException("Route could not be found",HttpStatus.NOT_FOUND);
        }

        Map<String, Object> route = routes.get(0);

        double distanceMeters = Double.parseDouble(route.get("distance").toString());

        double distanceKm = distanceMeters / 1000;
        
        System.out.println("Distance in meters: " + distanceMeters);
        System.out.println("Distance in KM: " + distanceKm);

        return distanceKm;
		} 
		catch (PriceCalculationException p) {
		    throw p;
		}
		catch (Exception e) {
			throw new PriceCalculationException("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
