package com.ait.app.ServiceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.Service.AddressService;
import com.ait.app.customExceptionHandler.AddressException;
import com.ait.app.model.Address;
import com.ait.app.model.User;
import com.ait.app.repository.AddressRepository;
import com.ait.app.repository.UserRepository;
import com.ait.app.requestBody.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {

	private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressRepository addressRepository;

	@Override
	public void saveAddress(Integer userId, AddressDto addDto) {

		logger.info("Create address request received. userId: {}", userId);

		if (userId == null || userId <= 0) {

			logger.warn("Invalid userId provided for address creation: {}", userId);

			throw new AddressException("Invalid user ID", HttpStatus.BAD_REQUEST);
		}

		if (addDto == null) {

			logger.warn("Address data is missing. userId: {}", userId);

			throw new AddressException("Address data is required", HttpStatus.BAD_REQUEST);
		}

		if (addDto.getAddressLabel() == null || addDto.getAddressLabel().trim().isEmpty()) {

			logger.warn("Address label is missing. userId: {}", userId);

			throw new AddressException("Address label is required", HttpStatus.BAD_REQUEST);
		}

		if (addDto.getStreetAddress() == null || addDto.getStreetAddress().trim().isEmpty()) {

			logger.warn("Street address is missing. userId: {}", userId);

			throw new AddressException("Street address is required", HttpStatus.BAD_REQUEST);
		}

		if (addDto.getCity() == null || addDto.getCity().trim().isEmpty()) {

			logger.warn("City is missing for address. userId: {}", userId);

			throw new AddressException("City is required", HttpStatus.BAD_REQUEST);
		}

		if (addDto.getPostalCode() == null || addDto.getPostalCode().trim().isEmpty()) {

			logger.warn("Postal code is missing. userId: {}", userId);

			throw new AddressException("Postal code is required", HttpStatus.BAD_REQUEST);
		}

		String postalCode = addDto.getPostalCode().trim();

		if (!postalCode.matches("^[0-9]{6}$")) {

			logger.warn("Invalid postal code provided. userId: {}", userId);

			throw new AddressException("Postal code must contain exactly 6 digits", HttpStatus.BAD_REQUEST);
		}

		User user;

		if (userRepository.findById(userId).isPresent()) {

			user = userRepository.findById(userId).get();

		} else {

			logger.warn("User not found while creating address. userId: {}", userId);

			throw new AddressException("User not found with ID: " + userId, HttpStatus.NOT_FOUND);
		}

		Address address = new Address();

		address.setAddressLabel(addDto.getAddressLabel().trim());
		address.setStreetAddress(addDto.getStreetAddress().trim());
		address.setCity(addDto.getCity().trim());
		address.setPostalCode(postalCode);

		if (addDto.getApartmentSuiteFloor() != null && !addDto.getApartmentSuiteFloor().trim().isEmpty()) {

			address.setApartmentSuiteFloor(addDto.getApartmentSuiteFloor().trim());
		}

		if (addDto.getLandmark() != null && !addDto.getLandmark().trim().isEmpty()) {

			address.setLandmark(addDto.getLandmark().trim());
		}

		if (addDto.getDeliveryInstructions() != null && !addDto.getDeliveryInstructions().trim().isEmpty()) {

			address.setDeliveryInstructions(addDto.getDeliveryInstructions().trim());
		}

		address.setUser(user);

		try {

			userRepository.save(user);
			addressRepository.save(address);

			logger.info("Address created successfully. userId: {}", userId);

		} catch (Exception e) {

			logger.error("Address could not be saved. userId: {}", userId);

			throw new AddressException("Unable to save address", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<Address> getAllAddressBtUserId(int userId) {

		logger.info("Get address request received. userId: {}", userId);

		if (userId <= 0) {

			logger.warn("Invalid userId for getting addresses: {}", userId);

			throw new AddressException("Invalid user ID", HttpStatus.BAD_REQUEST);
		}

		if (!userRepository.existsById(userId)) {

			logger.warn("User not found while getting addresses. userId: {}", userId);

			throw new AddressException("User not found with ID: " + userId, HttpStatus.NOT_FOUND);
		}

		List<Address> addressList = (List<Address>) addressRepository.findByUserId(userId);

		if (addressList.isEmpty()) {

			logger.warn("No address found for userId: {}", userId);

			throw new AddressException("No address found for user ID: " + userId, HttpStatus.NOT_FOUND);
		}

		logger.info("Addresses retrieved successfully. userId: {}", userId);

		return addressList;
	}
}