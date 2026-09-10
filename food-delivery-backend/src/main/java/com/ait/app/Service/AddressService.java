package com.ait.app.Service;

import java.util.List;

import com.ait.app.model.Address;
import com.ait.app.requestBody.AddressDto;

public interface AddressService {
	
	void saveAddress(Integer userId, AddressDto addDto);
	List<Address>getAllAddressBtUserId(int userId);

}
