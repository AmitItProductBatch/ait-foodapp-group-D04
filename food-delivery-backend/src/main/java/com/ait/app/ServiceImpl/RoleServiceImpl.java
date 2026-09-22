package com.ait.app.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.RoleService;
import com.ait.app.customExceptionHandler.RoleException;
import com.ait.app.customExceptionHandler.UserException;
import com.ait.app.model.Role;
import com.ait.app.repository.RoleRepository;
import com.ait.app.requestBody.RoleDto;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;

	@Override
	public ResponseEntity createRole(RoleDto roleDto) {

		if (roleDto.getName() == null || roleDto.getName().trim().isEmpty()) {

			throw new RoleException("Role name is required", HttpStatus.BAD_REQUEST);
		}

		if (roleDto.getDescription() == null || roleDto.getDescription().trim().isEmpty()) {

			throw new RoleException("Role description is required", HttpStatus.BAD_REQUEST);
		}

		String roleName = roleDto.getName().trim().toUpperCase();

		if (roleRepository.existsByName(roleName)) {

			throw new RoleException("Role " + roleName + " already exists", HttpStatus.CONFLICT);
		}

		Role role = new Role();

		role.setName(roleName);
		role.setDescription(roleDto.getDescription());

		Role savedRole = roleRepository.save(role);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
	}
}