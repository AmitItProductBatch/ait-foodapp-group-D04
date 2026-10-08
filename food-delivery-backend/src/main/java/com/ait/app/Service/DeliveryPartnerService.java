package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.requestBody.DeliveryPartnerRegisterDto;

public interface DeliveryPartnerService {

    ResponseEntity register(DeliveryPartnerRegisterDto dto);
}