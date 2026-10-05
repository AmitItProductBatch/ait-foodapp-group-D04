package com.ait.app.ServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.RoleService;
import com.ait.app.customExceptionHandler.RoleException;
import com.ait.app.model.Role;
import com.ait.app.repository.RoleRepository;
import com.ait.app.requestBody.RoleDto;

@Service
public class RoleServiceImpl implements RoleService {

	private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Autowired
	RoleRepository roleRepository;

	@Override
	public ResponseEntity createRole(RoleDto roleDto) {

		logger.info("Create role request received.");

		if (roleDto.getName() == null || roleDto.getName().trim().isEmpty()) {

			logger.warn("Role name is missing.");

			throw new RoleException("Role name is required", HttpStatus.BAD_REQUEST);
		}

		if (roleDto.getDescription() == null || roleDto.getDescription().trim().isEmpty()) {

			logger.warn("Role description is missing.");

			throw new RoleException("Role description is required", HttpStatus.BAD_REQUEST);
		}

		String roleName = roleDto.getName().trim().toUpperCase();

		if (roleRepository.existsByName(roleName)) {

			logger.warn("Role already exists. roleName: {}", roleName);

			throw new RoleException("Role " + roleName + " already exists", HttpStatus.CONFLICT);
		}

		Role role = new Role();

		role.setName(roleName);
		role.setDescription(roleDto.getDescription());

		try {

			Role savedRole = roleRepository.save(role);

			logger.info("Role created successfully. roleName: {}", roleName);

			return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);

		} catch (Exception e) {

			logger.error("Role could not be saved. roleName: {}", roleName);

			throw new RoleException("Unable to create role", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}