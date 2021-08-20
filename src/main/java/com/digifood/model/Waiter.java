package com.digifood.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "waiter")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class Waiter implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
	private User user;
	
	@JsonIgnore
	@OneToMany(mappedBy = "waiter")
	private List<TableOrder> orders;
	
	@OneToMany(mappedBy = "waiter")
	private List<RestaurantTable> tables;
	
	private String employeeNumber;
	
	private String transcriptionLanguage;
	private String country;
	
	public Waiter() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getTranscriptionLanguage() {
		return transcriptionLanguage;
	}
	public void setTranscriptionLanguage(String transcriptionLanguage) {
		this.transcriptionLanguage = transcriptionLanguage;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public List<TableOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<TableOrder> orders) {
		this.orders = orders;
	}

	public List<RestaurantTable> getTables() {
		return tables;
	}

	public void setTables(List<RestaurantTable> tables) {
		this.tables = tables;
	}
	
	
	
}
