package com.ait.app.ServiceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.UserService;
import com.ait.app.customExceptionHandler.UserException;
import com.ait.app.model.User;
import com.ait.app.repository.UserRepository;
import com.ait.app.requestBody.UserDto;
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	@Override
	public ResponseEntity addUser(User user) {
		
		if (user.getName()==null || user.getName().trim().isEmpty()||
			user.getEmail()==null || user.getEmail().trim().isEmpty()||
			user.getPass()==null || user.getPass().trim().isEmpty()||
			user.getRole()==null || user.getRole().trim().isEmpty()||
			user.getMobno()==null || user.getMobno().trim().isEmpty()) {
		
			throw new UserException("All fields are required..",HttpStatus.BAD_REQUEST );
		}
		
		if(userRepository.existsByEmail(user.getEmail())) {
			throw new UserException("User Already Exists.....", HttpStatus.CONFLICT);
		}
		try {
			User savedUser=userRepository.save(user);
			   UserDto dto = new UserDto();

		        dto.setName(savedUser.getName());
		        dto.setEmail(savedUser.getEmail());
		        dto.setRole(savedUser.getRole());
		        dto.setMobno(savedUser.getMobno());
		        dto.setCreatedDt(savedUser.getCreatedDt());

			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
			
		}catch (Exception e) {
			
			throw new UserException("Failed to save User", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@Override
	public ResponseEntity deleteUser(int id) {

	    if (userRepository.existsById(id)) {

	        userRepository.deleteById(id);
	        return ResponseEntity.status(HttpStatus.OK).body("User Deleted Successfully...");

	    } else {

	        throw new UserException("User not found",HttpStatus.NOT_FOUND);
	    }
	}

}
