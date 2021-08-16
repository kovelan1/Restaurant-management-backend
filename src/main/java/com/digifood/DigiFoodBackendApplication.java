package com.digifood;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.digifood.model.User;
import com.digifood.repository.UserRepository;

import jdk.jfr.internal.PrivateAccess;


@SpringBootApplication
@EnableJpaAuditing
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DigiFoodBackendApplication {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	 @PostConstruct
	    public void initData() {
	        List<User> users = Stream.of(
	                new User("admin@gmail.com", passwordEncoder.encode("admin"),"sam","bill","ROLE_admin",true ),
	               
	                new User("user", passwordEncoder.encode("user"),"john","doe","ROLE_user",true )
	        ).collect(Collectors.toList());
	        userRepository.saveAll(users);
	        
	        
	 }

	public static void main(String[] args) {
		SpringApplication.run(DigiFoodBackendApplication.class, args);
	}

}
