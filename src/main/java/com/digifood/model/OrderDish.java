package com.digifood.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class OrderDish {

	 @EmbeddedId
	 private OrderDishKey id;
	 
	 @JsonIgnore
	 @ManyToOne
	 @MapsId("orderId")
	 @JoinColumn(name = "order_id")
	 private TableOrder order;
	 
	 @ManyToOne
	 @MapsId("dishId")
	 @JoinColumn(name = "dish_id")
	 private Dish dish;
	 
	 private int quantity;
	 
	 public OrderDish() {
		// TODO Auto-generated constructor stub
	}

	public OrderDish(TableOrder order, Dish dish, int quantity) {
		super();
		this.id = new OrderDishKey(order.getId(), dish.getId());
		this.order = order;
		this.dish = dish;
		this.quantity = quantity;
	}

	public OrderDishKey getId() {
		return id;
	}

	public void setId(OrderDishKey id) {
		this.id = id;
	}

	public TableOrder getOrder() {
		return order;
	}

	public void setOrder(TableOrder order) {
		this.order = order;
	}

	public Dish getDish() {
		return dish;
	}

	public void setDish(Dish dish) {
		this.dish = dish;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	 
	
	 
}
