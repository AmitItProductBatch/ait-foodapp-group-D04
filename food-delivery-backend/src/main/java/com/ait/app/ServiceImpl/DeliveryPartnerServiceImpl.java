
package com.ait.app.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.DeliveryPartnerService;
import com.ait.app.customExceptionHandler.DeliveryPartnerNotFoundException;
import com.ait.app.model.DeliveryPartner;
import com.ait.app.repository.DeliveryPartnerRepository;
import com.ait.app.repository.OrderRepository;
import com.ait.app.requestBody.DeliveryPartnerAvailabilityDto;
import com.ait.app.requestBody.DeliveryPartnerRegisterDto;
import com.ait.app.requestBody.DeliveryPartnerRequestDto;
import com.ait.app.response.DeliveryPartnerResponseDto;

@Service
public class DeliveryPartnerServiceImpl implements DeliveryPartnerService {

	@Autowired
	DeliveryPartnerRepository repository;

	@Autowired
	OrderRepository orderRepository;

	@Override
	public ResponseEntity register(DeliveryPartnerRegisterDto dto) {

		if (dto.getName() == null || dto.getName().trim().isEmpty() || dto.getEmail() == null
				|| dto.getEmail().trim().isEmpty() || dto.getMobileNumber() == null
				|| dto.getMobileNumber().trim().isEmpty() || dto.getPassword() == null
				|| dto.getPassword().trim().isEmpty() || dto.getVehicleType() == null
				|| dto.getVehicleType().trim().isEmpty() || dto.getVehicleNumber() == null
				|| dto.getVehicleNumber().trim().isEmpty()) {

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

	@Override
	public ResponseEntity updateAvailability(DeliveryPartnerAvailabilityDto dto) {

		if (dto.getDeliveryPartnerId() == null) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delivery partner id is required");
		}

		if (dto.getAvailabilityStatus() == null || dto.getAvailabilityStatus().trim().isEmpty()) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Availability status is required");
		}

		String status = dto.getAvailabilityStatus().trim().toUpperCase();

		if (!status.equals("AVAILABLE") && !status.equals("UNAVAILABLE")) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status must be AVAILABLE or UNAVAILABLE");
		}

		DeliveryPartner partner = repository.findById(dto.getDeliveryPartnerId()).orElse(null);

		if (partner == null) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery partner not found");
		}

		if (status.equals("AVAILABLE")) {

			if (!partner.getAccountStatus().equalsIgnoreCase("ACTIVE")) {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delivery partner is not active");
			}

			if (!partner.isApproved()) {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delivery partner is not approved");
			}

			long activeOrders = orderRepository.countByDeliveryPartnerIdAndStatusIn(partner.getId(),
					java.util.Collections.singletonList("PLACED"));

			if (activeOrders >= 5) {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Maximum 5 active orders reached");
			}
		}

		partner.setAvailabilityStatus(status);

		repository.save(partner);

		return ResponseEntity.status(HttpStatus.OK).body("Availability updated successfully");
	}

	@Override
	public DeliveryPartnerResponseDto getDeliveryPartner(Integer id) {
		Optional<DeliveryPartner> o = repository.findById(id);
		if (o.isEmpty()) {
			throw new DeliveryPartnerNotFoundException("delivery partner not found with id " + id, HttpStatus.NOT_FOUND);
		}
		DeliveryPartner deliveryPartner = o.get();
		DeliveryPartnerResponseDto deliveryPartnerResponseDto = new DeliveryPartnerResponseDto();
	
		deliveryPartnerResponseDto.setId(deliveryPartner.getId());
		deliveryPartnerResponseDto.setEmail(deliveryPartner.getEmail());
		deliveryPartnerResponseDto.setMobileNumber(deliveryPartner.getMobileNumber());
		deliveryPartnerResponseDto.setName(deliveryPartner.getName());
		deliveryPartnerResponseDto.setAccountStatus(deliveryPartner.getAccountStatus());
		deliveryPartnerResponseDto.setVehicleType(deliveryPartner.getVehicleType());
        deliveryPartnerResponseDto.setVehicleNumber(deliveryPartner.getVehicleNumber());
        
		return deliveryPartnerResponseDto;
	}

	@Override
	public DeliveryPartnerResponseDto updateDeliveryPartner(int deliveryPartnerId,
			DeliveryPartnerRequestDto deliveryPartnerRequestDto) {
		Optional<DeliveryPartner> optional = repository.findById(deliveryPartnerId);

		if (optional.isEmpty()) {

			throw new DeliveryPartnerNotFoundException("delivery partner not found", HttpStatus.NOT_FOUND);
		}

		DeliveryPartner partner = optional.get();

		partner.setName(deliveryPartnerRequestDto.getName());
		partner.setMobileNumber(deliveryPartnerRequestDto.getMobileNumber());
		partner.setEmail(deliveryPartnerRequestDto.getEmail());
		partner.setVehicleType(deliveryPartnerRequestDto.getVehicleType());
		

		partner = repository.save(partner);

		DeliveryPartnerResponseDto response = new DeliveryPartnerResponseDto();

		response.setId(partner.getId());
		response.setName(partner.getName());
		response.setMobileNumber(partner.getMobileNumber());
		response.setEmail(partner.getEmail());
		response.setVehicleType(partner.getVehicleType());
		response.setVehicleNumber(partner.getVehicleNumber());
		response.setAccountStatus(partner.getAccountStatus());

		return response;
	}

	@Override
	public void deleteDeliveryPartner(int deliveryPartnerId) {
		if (!repository.existsById(deliveryPartnerId)) {
			throw new DeliveryPartnerNotFoundException("delivery partner not found", HttpStatus.NOT_FOUND);
		}
		repository.deleteById(deliveryPartnerId);
		
	}

	@Override
	public List<DeliveryPartnerResponseDto> getAllDeliveryPartner() {
		List<DeliveryPartner> list = repository.findAll();
		List<DeliveryPartnerResponseDto> deliveryPartnerList = new ArrayList();
		for (DeliveryPartner deliveryPartner : list) {
			DeliveryPartnerResponseDto deliveryPartnerResponseDto = new DeliveryPartnerResponseDto();
	
			deliveryPartnerResponseDto.setId(deliveryPartner.getId());
			deliveryPartnerResponseDto.setEmail(deliveryPartner.getEmail());
			deliveryPartnerResponseDto.setMobileNumber(deliveryPartner.getMobileNumber());
			deliveryPartnerResponseDto.setName(deliveryPartner.getName());
			deliveryPartnerResponseDto.setAccountStatus(deliveryPartner.getAccountStatus());
			deliveryPartnerResponseDto.setVehicleType(deliveryPartner.getVehicleType());
			deliveryPartnerResponseDto.setVehicleNumber(deliveryPartner.getVehicleNumber());
			deliveryPartnerList.add(deliveryPartnerResponseDto);
		}
		return deliveryPartnerList;
	}
}
