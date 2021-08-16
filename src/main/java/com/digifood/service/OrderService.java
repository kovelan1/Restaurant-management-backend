package com.digifood.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.digifood.exception.ResourceNotFoundException;
import com.digifood.model.*;
import com.digifood.repository.*;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private DishRepository dishRepository;
	
	@Autowired
	private OrderDishRepository orderDishRepository;


	public TableOrder createOrder(TableOrder order) {
		order.setStatus(OrderStatus.PENDING);
		TableOrder newOrder=orderRepository.save(order);
		
		List<OrderDish> orderDishs=new ArrayList<OrderDish>();
		
		order.getOrderDish().forEach(od->{
			OrderDish orderDish=new OrderDish(newOrder, od.getDish(), od.getQuantity());
			orderDishs.add(orderDish);
		});
		
		orderDishRepository.saveAll(orderDishs);
		return newOrder;
	}


	public TableOrder getOrderById(Long orderId) throws ResourceNotFoundException {
		return orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("oder not found"));
		
	}


	public TableOrder updateOrder(Long orderId, Map<Object, Object> feilds) throws ResourceNotFoundException {
		TableOrder order=orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("oder not found"));
		
		feilds.forEach((key,value)->{
			 Field field = ReflectionUtils.findField(TableOrder.class, (String)key);
			field.setAccessible(true);
			ReflectionUtils.setField(field, order, value);
		});
		
		return orderRepository.save(order);
	}
	

	public List<TableOrder> getAllOrders() {
		
		return orderRepository.findAll();
	}


	public void deleteOrderById(Long orderId) {
		orderRepository.deleteById(orderId);
	}
	
	
	
}
