package com.ait.app.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.DeliveryFeeService;
import com.ait.app.Service.DeliveryPartnerService;
import com.ait.app.model.DeliveryPartner;
import com.ait.app.repository.DeliveryPartnerRepository;
import com.ait.app.requestBody.DeliveryPartnerRegisterDto;

@Service
public class DeliveryPartnerServiceImpl implements DeliveryPartnerService {

    @Autowired
    DeliveryPartnerRepository repository;

    @Override
    public ResponseEntity register(DeliveryPartnerRegisterDto dto) {

        if (dto.getName() == null || dto.getName().trim().isEmpty()
                || dto.getEmail() == null || dto.getEmail().trim().isEmpty()
                || dto.getMobileNumber() == null || dto.getMobileNumber().trim().isEmpty()
                || dto.getPassword() == null || dto.getPassword().trim().isEmpty()
                || dto.getVehicleType() == null || dto.getVehicleType().trim().isEmpty()
                || dto.getVehicleNumber() == null || dto.getVehicleNumber().trim().isEmpty()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("All fields are required");
        }

        if (!dto.getMobileNumber().matches("^[6-9][0-9]{9}$")) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid mobile number");
        }

        if (repository.existsByEmail(dto.getEmail())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
        }

        if (repository.existsByMobileNumber(dto.getMobileNumber())) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mobile number already registered");
        }

        DeliveryPartner partner = new DeliveryPartner();

        partner.setName(dto.getName());
        partner.setEmail(dto.getEmail());
        partner.setMobileNumber(dto.getMobileNumber());
        partner.setPassword(dto.getPassword());
        partner.setVehicleType(dto.getVehicleType());
        partner.setVehicleNumber(dto.getVehicleNumber());


        repository.save(partner);

        return ResponseEntity.status(HttpStatus.CREATED).body("Delivery partner registered successfully");
    }

	
}