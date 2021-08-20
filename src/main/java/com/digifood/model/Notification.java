package com.digifood.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.digifood.DTO.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Notification extends AuditModel{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "receiver_id")
	private User receiver;
	
	@JsonIgnore
	@ManyToOne 
	@JoinColumn(name = "action_by_id")
	private User actionBy;
	
	@JsonIgnore
	@ManyToOne 
	@JoinColumn(name = "order_id")
	private TableOrder order;
	
	@Enumerated(EnumType.STRING)
	private NotificationEvent event;
	
	public Notification() {
		// TODO Auto-generated constructor stub
	}

	public Notification(User receiver, User actionBy, NotificationEvent event,TableOrder order) {
		super();
		this.receiver = receiver;
		this.actionBy = actionBy;
		this.event = event;
		this.order=order;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public User getActionBy() {
		return actionBy;
	}

	public void setActionBy(User actionBy) {
		this.actionBy = actionBy;
	}

	public NotificationEvent getEvent() {
		return event;
	}

	public void setEvent(NotificationEvent event) {
		this.event = event;
	}

	public TableOrder getOrder() {
		return order;
	}

	public void setOrder(TableOrder order) {
		this.order = order;
	}
	
	
}
