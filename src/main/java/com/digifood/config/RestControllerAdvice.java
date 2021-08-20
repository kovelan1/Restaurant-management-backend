package com.digifood.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice {
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handIllegalArgumentException(IllegalArgumentException exception){
		return new ResponseEntity<Exception>(new Exception("Invalid update action"),HttpStatus.BAD_REQUEST);
	}
	
}
