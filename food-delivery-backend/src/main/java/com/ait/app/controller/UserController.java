package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.UserService;
import com.ait.app.model.User;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("add/user")
	public ResponseEntity saveUser(@RequestBody User user) {
		
		return userService.addUser(user);
	}
	

}
