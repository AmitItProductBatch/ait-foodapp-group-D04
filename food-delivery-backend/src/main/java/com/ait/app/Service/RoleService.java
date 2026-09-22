package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.requestBody.RoleDto;

public interface RoleService {

	ResponseEntity createRole(RoleDto roleDto);
}
