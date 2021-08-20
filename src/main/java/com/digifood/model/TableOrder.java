package com.digifood.model;

import java.util.List;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.digifood.DTO.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class TableOrder extends AuditModel{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "table_id")
	private RestaurantTable table;
	
	private int persions;
	
	@OneToMany(mappedBy = "order")
	private List<OrderDish> orderDish;
	
	@ManyToOne
	@JoinColumn(name = "waiter_id")
	private Waiter waiter;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@JsonIgnore
	@OneToMany(mappedBy = "order")
	private List<Notification> notifications;
	
	public TableOrder() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public RestaurantTable getTable() {
		return table;
	}

	public void setTable(RestaurantTable table) {
		this.table = table;
	}

	public List<OrderDish> getOrderDish() {
		return orderDish;
	}

	public void setOrderDish(List<OrderDish> orderDish) {
		this.orderDish = orderDish;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public int getPersions() {
		return persions;
	}

	public void setPersions(int persions) {
		this.persions = persions;
	}

	public Waiter getWaiter() {
		return waiter;
	}

	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	
	
	
}
