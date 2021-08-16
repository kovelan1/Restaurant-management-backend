package com.digifood.DTO;

public class AuthenticationResponse {
	
	private String userName;
	private String role;
	private final String token;
	
	
	
	public AuthenticationResponse( String userName, String role, String token) {
		super();
		this.userName = userName;
		this.role = role;
		this.token = token;
	}



	public String getToken() {
		return token;
	}

	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getRole() {
		return role;
	}



	public void setRole(String role) {
		this.role = role;
	}
	
	
	
}
