package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.UserService;
import com.ait.app.model.User;

@RestController
@RequestMapping("api/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("register")
	public ResponseEntity saveUser(@RequestBody User user) {
		
		return userService.addUser(user);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deleteUser(@PathVariable Integer id) {

	    userService.deleteUser(id);

	    return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	

}
