package com.ait.app.ServiceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.UserService;
import com.ait.app.model.User;
import com.ait.app.repository.UserRepository;
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
		
			
			Map<String, String> errormsg=new HashMap<>();
			errormsg.put("error", "All fields are required..");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errormsg);
		}
		try {
			User savedUser=userRepository.save(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
			
		} catch (Exception e) {
	
			Map<String, String> errormsg=new HashMap<>();
			errormsg.put("error", "Failed to save user: "+e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errormsg);
		}
		   
		
	}

}
