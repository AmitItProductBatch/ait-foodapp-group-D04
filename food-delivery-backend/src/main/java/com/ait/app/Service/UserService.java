package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;

import com.ait.app.model.User;

import com.ait.app.requestBody.UserDto;

public interface UserService {

	ResponseEntity addUser(User user, String roleName);

	ResponseEntity deleteUser(int id);

	UserDto getUserById(int id);

	ResponseEntity updateUser(int id, UserDto dto);


}
