package com.ait.app.Service;

import com.ait.app.requestBody.OrderTotalRequestDto;
import com.ait.app.response.OrderTotalResponseDto;

public interface OrderTotalService {
	
	public OrderTotalResponseDto calculateOrdertotal(OrderTotalRequestDto reqDto);

}
