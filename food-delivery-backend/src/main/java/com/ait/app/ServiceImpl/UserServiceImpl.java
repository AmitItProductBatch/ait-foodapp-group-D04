package com.ait.app.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.UserService;
import com.ait.app.customExceptionHandler.UserException;
import com.ait.app.model.Address;
import com.ait.app.model.User;
import com.ait.app.repository.AddressRepository;
import com.ait.app.repository.UserRepository;
import com.ait.app.requestBody.AddressDto;
import com.ait.app.requestBody.UserDto;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressRepository addressRepository;

	@Override
	public ResponseEntity addUser(User user) {

		if (user.getName() == null || user.getName().trim().isEmpty() || user.getEmail() == null
				|| user.getEmail().trim().isEmpty() || user.getPass() == null || user.getPass().trim().isEmpty()
				|| user.getRole() == null || user.getRole().trim().isEmpty() || user.getMobno() == null
				|| user.getMobno().trim().isEmpty()) {

			throw new UserException("All fields are required..", HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(user.getEmail())) {
			throw new UserException("User Already Exists.....", HttpStatus.CONFLICT);
		}
		try {

			if (user.getAddresses() != null) {
				for (Address address : user.getAddresses()) {
					address.setUser(user);
				}
			}

			User savedUser = userRepository.save(user);
			UserDto dto = new UserDto();

			dto.setName(savedUser.getName());
			dto.setEmail(savedUser.getEmail());
			dto.setRole(savedUser.getRole());
			dto.setMobno(savedUser.getMobno());
			dto.setCreatedDt(savedUser.getCreatedDt());

			List<AddressDto> addressDtos = new ArrayList();

			if (savedUser.getAddresses() != null) {

				for (Address address : savedUser.getAddresses()) {

					AddressDto addressDto = new AddressDto();

					addressDto.setId(address.getId());
					addressDto.setAddressLabel(address.getAddressLabel());
					addressDto.setStreetAddress(address.getStreetAddress());
					addressDto.setApartmentSuiteFloor(address.getApartmentSuiteFloor());
					addressDto.setLandmark(address.getLandmark());
					addressDto.setCity(address.getCity());
					addressDto.setPostalCode(address.getPostalCode());
					addressDto.setDeliveryInstructions(address.getDeliveryInstructions());

					addressDtos.add(addressDto);
				}
			}

			dto.setAddresses(addressDtos);

			return ResponseEntity.status(HttpStatus.CREATED).body(dto);

		} catch (Exception e) {

			throw new UserException("Failed to save User", HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity deleteUser(int id) {

		if (userRepository.existsById(id)) {

			userRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("User Deleted Successfully...");

		} else {

			throw new UserException("User not found", HttpStatus.NOT_FOUND);
		}
	}

	public UserDto getUserById(int id) {

		try {

			User user = userRepository.findById(id).get();

			if (userRepository.existsById(id)) {

				UserDto userDto = new UserDto();

				userDto.setCreatedDt(user.getCreatedDt());
				userDto.setEmail(user.getEmail());
				userDto.setMobno(user.getMobno());
				userDto.setName(user.getName());
				userDto.setRole(user.getRole());

				return userDto;
			}

			throw new UserException("User not found", HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			throw new UserException("User not found of id " + id, HttpStatus.NOT_FOUND);

		}
	}

	@Override
	@Transactional
	public ResponseEntity updateUser(int id, UserDto dto) {

		Optional<User> o = userRepository.findById(id);

		if (o.isEmpty()) {
			throw new UserException("User not found of this id " + id, HttpStatus.NOT_FOUND);
		}

		User user = o.get();

		if (dto.getMobno() != null) {
			user.setMobno(dto.getMobno());
		}
		if (dto.getName() != null) {
			user.setName(dto.getName());
		}
		if (dto.getEmail() != null) {
			user.setEmail(dto.getEmail());
		}

		List<AddressDto> addresses = dto.getAddresses();

		if (addresses == null || addresses.isEmpty()) {
			throw new UserException("Address is required", HttpStatus.BAD_REQUEST);
		}

		AddressDto addressDto = addresses.get(0);

		Optional<Address> optionalAddress = addressRepository.findById(addressDto.getId());

		if (optionalAddress.isEmpty()) {
			throw new UserException("Address not found", HttpStatus.NOT_FOUND);
		}

		Address address = optionalAddress.get();

		if (addressDto.getCity() != null) {
			address.setCity(addressDto.getCity());
		}

		if (addressDto.getStreetAddress() != null) {
			address.setStreetAddress(addressDto.getStreetAddress());
		}

		if (addressDto.getApartmentSuiteFloor() != null) {
			address.setApartmentSuiteFloor(addressDto.getApartmentSuiteFloor());
		}

		if (addressDto.getAddressLabel() != null) {
			address.setAddressLabel(addressDto.getAddressLabel());
		}

		if (addressDto.getLandmark() != null) {
			address.setLandmark(addressDto.getLandmark());
		}

		if (addressDto.getPostalCode() != null) {
			address.setPostalCode(addressDto.getPostalCode());
		}

		if (addressDto.getDeliveryInstructions() != null) {
			address.setDeliveryInstructions(addressDto.getDeliveryInstructions());
		}

		addressRepository.save(address);

		userRepository.save(user);

		return ResponseEntity.status(HttpStatus.OK).body(user);

	}
}
