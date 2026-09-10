package com.ait.app.ServiceImpl;

import java.util.List;
import java.util.Optional;

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

	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressRepository addressRepository;

	@Override
	public void saveAddress(Integer userId, AddressDto addDto) {

		if (userId == null || userId <= 0) {
			throw new AddressException("Invalid user ID", HttpStatus.BAD_REQUEST);
		}

		if (addDto == null) {
			throw new AddressException("Address data is required", HttpStatus.BAD_REQUEST);
		}

		if (addDto.getAddressLabel() == null || addDto.getAddressLabel().trim().isEmpty()) {

			throw new AddressException("Address label is required", HttpStatus.BAD_REQUEST);
		}

		if (addDto.getStreetAddress() == null || addDto.getStreetAddress().trim().isEmpty()) {

			throw new AddressException("Street address is required", HttpStatus.BAD_REQUEST);
		}

		if (addDto.getCity() == null || addDto.getCity().trim().isEmpty()) {

			throw new AddressException("City is required", HttpStatus.BAD_REQUEST);
		}

		if (addDto.getPostalCode() == null || addDto.getPostalCode().trim().isEmpty()) {

			throw new AddressException("Postal code is required", HttpStatus.BAD_REQUEST);
		}

		String postalCode = addDto.getPostalCode().trim();

		if (!postalCode.matches("^[0-9]{6}$")) {

			throw new AddressException("Postal code must contain exactly 6 digits", HttpStatus.BAD_REQUEST);
		}

		User user;

		if (userRepository.findById(userId).isPresent()) {
			user = userRepository.findById(userId).get();
		} else {
			throw new AddressException("User not found with ID: " + userId, HttpStatus.NOT_FOUND);
		}

		Address address = new Address();

		address.setAddressLabel(addDto.getAddressLabel().trim());
		address.setStreetAddress(addDto.getStreetAddress().trim());
		address.setCity(addDto.getCity().trim());
		address.setPostalCode(postalCode);

		if (addDto.getApartmentSuiteFloor() != null && !addDto.getApartmentSuiteFloor().trim().isEmpty()) 
		{

			address.setApartmentSuiteFloor(addDto.getApartmentSuiteFloor().trim());
		}

		if (addDto.getLandmark() != null && !addDto.getLandmark().trim().isEmpty())
		{

			address.setLandmark(addDto.getLandmark().trim());
		}

		if (addDto.getDeliveryInstructions() != null && !addDto.getDeliveryInstructions().trim().isEmpty()) 
		{

			address.setDeliveryInstructions(addDto.getDeliveryInstructions().trim());
		}

		address.setUser(user);

		userRepository.save(user);
		addressRepository.save(address);
	}

	@Override
	public List<Address> getAllAddressBtUserId(int userId) {

		if (userId <= 0) {
			throw new AddressException("Invalid user ID", HttpStatus.BAD_REQUEST);
		}

		if (!userRepository.existsById(userId)) {
			throw new AddressException("User not found with ID: " + userId, HttpStatus.NOT_FOUND);
		}

		List<Address> addressList = (List<Address>) addressRepository.findByUserId(userId);

		if (addressList.isEmpty()) {
			throw new AddressException("No address found for user ID: " + userId, HttpStatus.NOT_FOUND);
		}

		return addressList;
	}
}
