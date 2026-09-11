package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.UserService;
import com.ait.app.model.User;
import com.ait.app.requestBody.UserDto;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("add/user")
	public ResponseEntity saveUser(@RequestBody User user) {

		return userService.addUser(user);
	}

	@DeleteMapping("user/delete/{id}")
	public ResponseEntity deleteUser(@PathVariable int id) {

		return userService.deleteUser(id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable int id) {

		UserDto response = userService.getUserById(id);

		return ResponseEntity.ok(response);
	}
}
