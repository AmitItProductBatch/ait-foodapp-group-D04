package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.model.User;
import com.ait.app.requestBody.UserDto;

public interface UserService {

	ResponseEntity addUser(User user);

	ResponseEntity deleteUser(int id);

	UserDto getUserById(int id);
}
