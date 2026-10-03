package com.ait.app.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.Service.DeliveryFeeService;
import com.ait.app.Service.OrderTotalService;
import com.ait.app.customExceptionHandler.PriceCalculationException;
import com.ait.app.model.CartItems;
import com.ait.app.repository.CartItemRepository;
import com.ait.app.requestBody.DeliveryFeeRequestDto;
import com.ait.app.requestBody.OrderTotalRequestDto;
import com.ait.app.response.DeliveryFeeResponseDto;
import com.ait.app.response.OrderTotalResponseDto;
import com.ait.app.response.PriceCalculationResponse;

@Service
public class OrderTotalServiceImpl implements OrderTotalService {

	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	DeliveryFeeService deliveryFeeService;

	@Override
	public OrderTotalResponseDto calculateOrdertotal(OrderTotalRequestDto reqDto) {
        try {
		List<CartItems> cartItems = cartItemRepository.findByCartId(reqDto.getCartId());

		if (cartItems.isEmpty()) {
			throw new PriceCalculationException("Cart is empty", HttpStatus.BAD_REQUEST);
		}
		
		List<PriceCalculationResponse> items = new ArrayList<>();
		
		double itemSubtotal = 0;
		
		for (CartItems item : cartItems) {

			itemSubtotal = itemSubtotal + item.getSubtotal();
			PriceCalculationResponse itemResponse = new PriceCalculationResponse();

			itemResponse.setItemId(item.getMenuItem().getId());
			itemResponse.setItemName(item.getMenuItem().getName());
			itemResponse.setUnitPrice(item.getUnitprice());
			itemResponse.setQuantity(item.getQuantity());
			itemResponse.setSubtotal(item.getSubtotal());

			items.add(itemResponse);
		}

		double taxRate = 0.05;
		double taxAmount = itemSubtotal * taxRate;

		DeliveryFeeRequestDto deliveryRequest = new DeliveryFeeRequestDto();
		deliveryRequest.setCartId(reqDto.getCartId());
		deliveryRequest.setRestaurantId(reqDto.getRestaurantId());
		deliveryRequest.setAddressId(reqDto.getAddressId());

		DeliveryFeeResponseDto deliveryResponse = deliveryFeeService.calculateDeliveryFeeByDistance(deliveryRequest);
		double deliveryFee = deliveryResponse.getDeliveryFee();

		double discountAmount = 0;

		double orderTotal = itemSubtotal + taxAmount + deliveryFee - discountAmount;
		
		OrderTotalResponseDto response = new OrderTotalResponseDto();

		response.setItems(items);
		response.setItemSubtotal(itemSubtotal);
		response.setTaxAmount(taxAmount);
		response.setDeliveryFee(deliveryFee);
		response.setDiscountAmount(discountAmount);
		response.setOrderTotal(orderTotal);

		return response;
        }catch (PriceCalculationException p) {
            throw p;
        }
        catch (Exception e) {
			throw new PriceCalculationException("Unable to calculate order total", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
