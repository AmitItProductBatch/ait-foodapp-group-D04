package com.ait.app.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.Service.DeliveryFeeService;
import com.ait.app.Service.GeocodingService;
import com.ait.app.Service.RoutingService;
import com.ait.app.customExceptionHandler.PriceCalculationException;
import com.ait.app.model.Address;
import com.ait.app.model.CartItems;
import com.ait.app.model.DeliveryRules;
import com.ait.app.model.Restaurant;
import com.ait.app.repository.AddressRepository;
import com.ait.app.repository.CartItemRepository;
import com.ait.app.repository.DeliveryRuleRepo;
import com.ait.app.repository.RestaurantRepo;
import com.ait.app.requestBody.DeliveryFeeRequestDto;
import com.ait.app.response.DeliveryFeeResponseDto;

@Service
public class DeliveryFeeServiceImpl implements DeliveryFeeService {

	@Autowired
	RestaurantRepo restaurantRepo;

	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	GeocodingService geocodingService;
	
	@Autowired
	RoutingService rountingService;
	
	@Autowired
	DeliveryRuleRepo deliveryRuleRepo;
	
	@Autowired
	CartItemRepository cartItemRepository;

	@Override
	public DeliveryFeeResponseDto calculateDeliveryFeeByDistance(DeliveryFeeRequestDto reqDto) {
	     try {
		Optional<Restaurant> restaurant = restaurantRepo.findById(reqDto.getRestaurantId());
		if (restaurant.isEmpty()) {
			throw new PriceCalculationException("Restaurant Not Found", HttpStatus.NOT_FOUND);
		}
		Restaurant restaurantObj = restaurant.get();
		

		Optional<Address> address = addressRepository.findById(reqDto.getAddressId());
		if (address.isEmpty()) {

			throw new PriceCalculationException("Address Not Found", HttpStatus.NOT_FOUND);
		}
		Address addressObj = address.get();
		
		   
		
	    double[] restaurantCoordinates = geocodingService.getCoordinates(restaurantObj.getAddress());
		
	    String deliveryAddress = addressObj.getStreetAddress() + ", "
	            + addressObj.getCity() + ", "
	            + addressObj.getPostalCode();
	    double[] addressCoordinates = geocodingService.getCoordinates(deliveryAddress);
	
	   
	    
	    double distanceKm = rountingService.getDistance(
	            restaurantCoordinates[0],
	            restaurantCoordinates[1],
	            addressCoordinates[0],
	            addressCoordinates[1]);

	    List<DeliveryRules> rules =deliveryRuleRepo.findByActive(true);
	    if (rules.isEmpty()) {
	        throw new PriceCalculationException("Delivery rule not found",HttpStatus.NOT_FOUND);
	    }
	    DeliveryRules rule = rules.get(0);
	    
	    if (distanceKm > rule.getMaxDeliveryRadius()) {
	        throw new PriceCalculationException("Delivery address is outside delivery radius",HttpStatus.BAD_REQUEST);
	    }
	
	    
	
	    List<CartItems> cartItems =
	            cartItemRepository.findByCartId(reqDto.getCartId());

	    if (cartItems.isEmpty()) {
	        throw new PriceCalculationException("Cart is empty",HttpStatus.BAD_REQUEST);
	    }

	   
	    double subtotal = 0;

	    for (CartItems item : cartItems) {
	        subtotal = subtotal + item.getSubtotal();
	    }
	    
	    
	    double deliveryFee;
	    
	    if (subtotal >= rule.getFreeDeliveryThreshold()) {
	        deliveryFee = 0;
	    } else {
	        deliveryFee = rule.getBaseFee() + (distanceKm * rule.getPerKmRate());
	    }
	    
	    
	    DeliveryFeeResponseDto response = new DeliveryFeeResponseDto();
	   	response.setDistanceKm(distanceKm);
	    response.setDeliveryFee(deliveryFee);

	    return response;
	     }
	     catch (PriceCalculationException p) {
	            throw p;
	        }
	     catch (Exception e) {
			throw new PriceCalculationException("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		} 
	}

	
}
