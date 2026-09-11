package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.model.User;
import com.ait.app.response.UserProfileResponse;

public interface UserService {

	ResponseEntity addUser(User user);

	ResponseEntity deleteUser(int id);

	UserProfileResponse getUserById(int id);
}
