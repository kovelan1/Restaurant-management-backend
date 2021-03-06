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
@Table(name = "customer")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class Customer implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "user_id")
	private User user;
	
	@JsonIgnore
	@OneToMany(mappedBy = "customer")
	private List<TableOrder> orders;
	
	public Customer() {
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

	public List<TableOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<TableOrder> orders) {
		this.orders = orders;
	}
	
	
}
