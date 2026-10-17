
package com.ait.app.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.DeliveryPartnerService;
import com.ait.app.customExceptionHandler.DeliveryPartnerNotFoundException;
import com.ait.app.customExceptionHandler.DeliveryRuleException;
import com.ait.app.customExceptionHandler.OrderException;
import com.ait.app.model.DeliveryPartner;
import com.ait.app.model.DeliveryRules;
import com.ait.app.model.Order;
import com.ait.app.model.OrderItem;
import com.ait.app.repository.DeliveryPartnerRepository;
import com.ait.app.repository.DeliveryRuleRepo;
import com.ait.app.repository.OrderRepository;
import com.ait.app.requestBody.DeliveryPartnerAvailabilityDto;
import com.ait.app.requestBody.DeliveryPartnerRegisterDto;
import com.ait.app.requestBody.DeliveryPartnerRequestDto;
import com.ait.app.response.DeliveryEarningsResponse;
import com.ait.app.response.DeliveryHistoryResponse;
import com.ait.app.response.DeliveryPartnerResponseDto;

@Service
public class DeliveryPartnerServiceImpl implements DeliveryPartnerService {

	@Autowired
	DeliveryPartnerRepository repository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	DeliveryRuleRepo deliveryRuleRepo;

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
  
  @Override
	public ResponseEntity<String> acceptOrder(int orderId, Integer deliveryPartnerId) {
		try {
			if (deliveryPartnerId == null) {
				throw new OrderException("Delivery partner ID is required", HttpStatus.BAD_REQUEST);
			}

			Optional<Order> orderOptional = orderRepository.findById(orderId);

			if (orderOptional.isEmpty()) {
				throw new OrderException("Order not found", HttpStatus.NOT_FOUND);
			}

			Order order = orderOptional.get();

			if (!"PENDING".equals(order.getDeliveryPartnerStatus())) {
				throw new OrderException("Delivery request is already processed", HttpStatus.BAD_REQUEST);
			}

			order.setDeliveryPartnerId(deliveryPartnerId);
			order.setDeliveryPartnerStatus("ACCEPTED");
			order.setStatus("ACCEPTED");

			orderRepository.save(order);

			return ResponseEntity.ok("Delivery request accepted by delivery partner");
		} catch (OrderException e) {
			throw e;
		} catch (Exception e) {
			throw new OrderException("Unexpected error while accepting order", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> rejectOrder(int orderId, Integer deliveryPartnerId) {
		try {
			if (deliveryPartnerId == null) {
				throw new OrderException("Delivery partner ID is required", HttpStatus.BAD_REQUEST);
			}

			Optional<Order> orderOptional = orderRepository.findById(orderId);

			if (orderOptional.isEmpty()) {
				throw new OrderException("Order not found", HttpStatus.NOT_FOUND);
			}

			Order order = orderOptional.get();

			if (!"PENDING".equals(order.getDeliveryPartnerStatus())) {
				throw new OrderException("Delivery request is already processed", HttpStatus.BAD_REQUEST);
			}

			order.setDeliveryPartnerId(deliveryPartnerId);
			order.setDeliveryPartnerStatus("REJECTED");
			order.setDeliveryPartnerId(null);

			orderRepository.save(order);

			return ResponseEntity.ok("Delivery request rejected by delivery partner");
		} catch (OrderException e) {
			throw e;
		} catch (Exception e) {
			throw new OrderException("Unexpected error while rejecting order", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<String> updateOrderStatus(int orderId, String status) {

	    Order order = orderRepository.getById(orderId);

	    if (order == null) {
	        throw new OrderException("Order not found", HttpStatus.NOT_FOUND);
	    }

	    if (status == null || status.trim().isEmpty()) {
	        throw new OrderException("Status is required", HttpStatus.BAD_REQUEST);
	    }

	    status = status.trim().toUpperCase();

	    String currentStatus = order.getStatus();

	     if ("PLACED".equals(currentStatus) && "PICKED_UP".equals(status)) {

	        order.setStatus("PICKED_UP");

	    } else if ("PICKED_UP".equals(currentStatus) && "DELIVERED".equals(status)) {

	        order.setStatus("DELIVERED");

	    } else {
	        throw new OrderException(
	                "Invalid status transition from " + currentStatus + " to " + status,
	                HttpStatus.BAD_REQUEST);
	    }

	    orderRepository.save(order);

	    return ResponseEntity.ok("Order status updated to " + status);
	}

	@Override
	public ResponseEntity<List<DeliveryHistoryResponse>> getDeliveryHistory(Integer deliveryPartnerId) {

		validateDeliveryPartner(deliveryPartnerId);

		DeliveryRules rule = getActiveDeliveryRule();
		List<Order> orders = orderRepository.findByDeliveryPartnerIdAndStatusOrderByUpdatedAtDesc(
				deliveryPartnerId, "DELIVERED");
		List<DeliveryHistoryResponse> responseList = new ArrayList<>();

		for (Order order : orders) {
			DeliveryHistoryResponse response = new DeliveryHistoryResponse();
			response.setOrderId(order.getId());
			response.setDeliveryDate(getDeliveryDate(order));
			response.setEarnings(calculateEarnings(order, rule));
			responseList.add(response);
		}

		return ResponseEntity.status(HttpStatus.OK).body(responseList);
	}

	@Override
	public ResponseEntity<DeliveryEarningsResponse> getEarnings(Integer deliveryPartnerId) {

		validateDeliveryPartner(deliveryPartnerId);

		DeliveryRules rule = getActiveDeliveryRule();
		List<Order> orders = orderRepository.findByDeliveryPartnerIdAndStatusOrderByUpdatedAtDesc(
				deliveryPartnerId, "DELIVERED");

		LocalDate today = LocalDate.now();
		LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
		LocalDateTime todayStart = today.atStartOfDay();
		LocalDateTime tomorrowStart = today.plusDays(1).atStartOfDay();
		LocalDateTime weekStartTime = weekStart.atStartOfDay();
		LocalDateTime monthStartTime = monthStart.atStartOfDay();

		double dailyEarnings = 0;
		double weeklyEarnings = 0;
		double monthlyEarnings = 0;
		double totalEarnings = 0;

		for (Order order : orders) {
			double earnings = calculateEarnings(order, rule);
			LocalDateTime deliveryDate = getDeliveryDate(order);

			totalEarnings = totalEarnings + earnings;

			if (deliveryDate != null && !deliveryDate.isBefore(todayStart)
					&& deliveryDate.isBefore(tomorrowStart)) {
				dailyEarnings = dailyEarnings + earnings;
			}

			if (deliveryDate != null && !deliveryDate.isBefore(weekStartTime)) {
				weeklyEarnings = weeklyEarnings + earnings;
			}

			if (deliveryDate != null && !deliveryDate.isBefore(monthStartTime)) {
				monthlyEarnings = monthlyEarnings + earnings;
			}
		}

		DeliveryEarningsResponse response = new DeliveryEarningsResponse();
		response.setDailyEarnings(dailyEarnings);
		response.setWeeklyEarnings(weeklyEarnings);
		response.setMonthlyEarnings(monthlyEarnings);
		response.setTotalEarnings(totalEarnings);
		response.setTotalCompletedDeliveries(orders.size());

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	private void validateDeliveryPartner(Integer deliveryPartnerId) {

		if (deliveryPartnerId == null) {
			throw new DeliveryPartnerNotFoundException("delivery partner id is required", HttpStatus.BAD_REQUEST);
		}

		if (!repository.existsById(deliveryPartnerId)) {
			throw new DeliveryPartnerNotFoundException("delivery partner not found with id " + deliveryPartnerId,
					HttpStatus.NOT_FOUND);
		}
	}

	private DeliveryRules getActiveDeliveryRule() {

		List<DeliveryRules> rules = deliveryRuleRepo.findByActive(true);

		if (rules == null || rules.isEmpty()) {
			throw new DeliveryRuleException("Delivery rule not found", HttpStatus.NOT_FOUND);
		}

		return rules.get(0);
	}

	private LocalDateTime getDeliveryDate(Order order) {

		if (order.getUpdatedAt() != null) {
			return order.getUpdatedAt();
		}

		return order.getCreatedAt();
	}

	private double calculateEarnings(Order order, DeliveryRules rule) {

		if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
			return rule.getBaseFee();
		}

		double itemSubtotal = 0;

		for (OrderItem item : order.getOrderItems()) {
			if (item.getSubtotal() != null) {
				itemSubtotal = itemSubtotal + item.getSubtotal().doubleValue();
			}
		}

		double taxAmount = itemSubtotal * 0.05;
		double deliveryPayment = order.getTotalAmount() - itemSubtotal - taxAmount;

		if (deliveryPayment < rule.getBaseFee()) {
			return rule.getBaseFee();
		}

		return deliveryPayment;
	}
}
