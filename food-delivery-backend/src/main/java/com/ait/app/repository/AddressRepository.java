package com.ait.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>{
	 List<Address> findByUserId(int userId);
}
