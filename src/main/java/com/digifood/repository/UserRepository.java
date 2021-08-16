package com.digifood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digifood.model.User;
import java.lang.String;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String>{

	User findByUsername(String username);
	
	List<User> findByRole(String role);
}
