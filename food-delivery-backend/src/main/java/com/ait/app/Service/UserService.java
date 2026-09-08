package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.model.User;

public interface UserService {

	ResponseEntity addUser(User user);
}
