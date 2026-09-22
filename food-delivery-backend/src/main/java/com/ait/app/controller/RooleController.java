package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.RoleService;
import com.ait.app.requestBody.RoleDto;

@RestController
@RequestMapping("/api/roles")
public class RooleController {

	@Autowired
	RoleService roleService;

	@PostMapping
	public ResponseEntity createRole(@RequestBody RoleDto roleDto) {

		return roleService.createRole(roleDto);
	}
}
