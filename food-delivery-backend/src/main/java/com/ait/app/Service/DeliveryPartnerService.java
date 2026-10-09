package com.ait.app.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ait.app.requestBody.DeliveryPartnerAvailabilityDto;
import com.ait.app.requestBody.DeliveryPartnerRegisterDto;
import com.ait.app.requestBody.DeliveryPartnerRequestDto;
import com.ait.app.response.DeliveryPartnerResponseDto;

public interface DeliveryPartnerService {

	ResponseEntity register(DeliveryPartnerRegisterDto dto);
	DeliveryPartnerResponseDto getDeliveryPartner(Integer id);
    DeliveryPartnerResponseDto updateDeliveryPartner(int deliveryPartnerId,DeliveryPartnerRequestDto deliveryPartnerRequestDto);
    void deleteDeliveryPartner(int deliveryPartnerId);
    List<DeliveryPartnerResponseDto> getAllDeliveryPartner();
	ResponseEntity updateAvailability(DeliveryPartnerAvailabilityDto dto);
}
