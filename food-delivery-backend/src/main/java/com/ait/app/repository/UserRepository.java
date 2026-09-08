package com.ait.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
