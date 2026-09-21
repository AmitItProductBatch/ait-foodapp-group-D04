package com.ait.app.ServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.PriceCalculationService;
import com.ait.app.customExceptionHandler.PriceCalculationException;
import com.ait.app.model.MenuItem;
import com.ait.app.repository.CartItemRepository;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.requestBody.PriceCalculationRequestDto;
import com.ait.app.response.PriceCalculationResponse;

@Service
public class PriceCalServiceImpl implements PriceCalculationService {

	@Autowired
	MenuItemRepository menuItemRepository;

	@Override
	public ResponseEntity<PriceCalculationResponse> calculatePrice(PriceCalculationRequestDto priceDto) {

		if (priceDto.getQuantity() <= 0) {

			throw new PriceCalculationException("Quantity must be greater than zero", HttpStatus.BAD_REQUEST);

		}

		Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(priceDto.getItemId());

		if (optionalMenuItem.isEmpty()) {

			throw new PriceCalculationException("Menu item not found", HttpStatus.NOT_FOUND);

		}
		try {

			MenuItem menuItem = optionalMenuItem.get();

			double unitPrice = menuItem.getPrice();

			double subtotal = unitPrice * priceDto.getQuantity();

			PriceCalculationResponse priceResponse = new PriceCalculationResponse();

			priceResponse.setItemId(menuItem.getId());
			priceResponse.setItemName(menuItem.getName());
			priceResponse.setUnitPrice(unitPrice);
			priceResponse.setQuantity(priceDto.getQuantity());
			priceResponse.setSubtotal(subtotal);

			return new ResponseEntity<PriceCalculationResponse>(priceResponse, HttpStatus.OK);

		} catch (Exception e) {
			throw new PriceCalculationException("Unable to calculate price", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
