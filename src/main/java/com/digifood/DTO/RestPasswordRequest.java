package com.digifood.DTO;

public class RestPasswordRequest {

	private String userName;
	private String password;
	private int token;
	
	public RestPasswordRequest() {
		// TODO Auto-generated constructor stub
	}
	
	public RestPasswordRequest(String userName, String password, int token) {
		super();
		this.userName = userName;
		this.password = password;
		this.token = token;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getToken() {
		return token;
	}
	public void setToken(int token) {
		this.token = token;
	}
	
	
}
