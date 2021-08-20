package com.digifood.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import com.digifood.exception.InvalidTokenException;
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
	private TableRepository tableRepository;
	
	@Autowired 
	private NotificationService notificationService;
	
	@Autowired
	private OrderDishRepository orderDishRepository;
	
	@Autowired
	private WaiterRepository waiterRepository;
	
	@Autowired
	private UserService userService;


	public TableOrder createOrder(TableOrder order,String username) throws ResourceNotFoundException {
		order.setStatus(OrderStatus.PENDING);
		order.setCustomer(userService.getUserByUsername(username).getCustomer());
		RestaurantTable table=tableRepository.findById(order.getTable().getId()).orElseThrow(()-> new ResourceNotFoundException("Table not found"));
		order.setWaiter(table.getWaiter());
		TableOrder newOrder=orderRepository.save(order);
		
		List<OrderDish> orderDishs=new ArrayList<OrderDish>();
		
		order.getOrderDish().forEach(od->{
			OrderDish orderDish=new OrderDish(newOrder, od.getDish(), od.getQuantity());
			orderDishs.add(orderDish);
		});
		
		orderDishRepository.saveAll(orderDishs);
		userService.getUsersByRole("ROLE_cook").forEach(cook->{
			notificationService.createNotification(cook, newOrder.getCustomer().getUser(), NotificationEvent.order_created,newOrder);
		});
		
		notificationService.createNotification(newOrder.getWaiter().getUser(), newOrder.getCustomer().getUser(), NotificationEvent.order_created,newOrder);
		
//		userService.getUsersByRole("ROLE_waiter").forEach(waiter->{
//			notificationService.createNotification(waiter, newOrder.getCustomer().getUser(), NotificationEvent.order_created,newOrder);
//		});
		
		return newOrder;																		
	}
	
	
	public TableOrder getOrderById(Long orderId) throws ResourceNotFoundException {
		return orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("oder not found"));
		
	}


	@Transactional
	public TableOrder updateOrder(Long orderId, Map<Object, Object> feilds, String username) throws ResourceNotFoundException, InvalidTokenException {
		TableOrder order=orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("oder not found"));
		User actionBy=userService.getUserByUsername(username);
		if(feilds.containsKey("status") && statusOwner(feilds.get("status").toString() ,actionBy)) {
		
		}
		
		feilds.forEach((key,value)->{
			Field field = ReflectionUtils.findField(TableOrder.class, (String)key);
			field.setAccessible(true);
			
				if(key.equals("status") ) {
					switch ((String)value) {
					case "COOKING":
						ReflectionUtils.setField(field, order, OrderStatus.COOKING);
						notificationService.createNotification(order.getCustomer().getUser(), actionBy, NotificationEvent.order_cooking, order);
						break;
					case "READY":
						ReflectionUtils.setField(field, order, OrderStatus.READY);
						notificationService.createNotification(order.getCustomer().getUser(), actionBy, NotificationEvent.order_ready, order);
						notificationService.createNotification(order.getWaiter().getUser(), actionBy, NotificationEvent.order_ready, order);
						break;
					case "SERVED":
						ReflectionUtils.setField(field, order, OrderStatus.SERVED);
						notificationService.createNotification(order.getCustomer().getUser(), actionBy, NotificationEvent.order_served, order);
						break;
					case "PAID":
						ReflectionUtils.setField(field, order, OrderStatus.PAID);
						notificationService.createNotification(order.getCustomer().getUser(), actionBy, NotificationEvent.order_paid, order);
						notificationService.createNotification(order.getWaiter().getUser(), actionBy, NotificationEvent.order_paid, order);
						break;
					case "COMPLETED":
						ReflectionUtils.setField(field, order, OrderStatus.COMPLETED);
						notificationService.createNotification(order.getCustomer().getUser(), actionBy, NotificationEvent.order_completed, order);
						notificationService.createNotification(order.getWaiter().getUser(), actionBy, NotificationEvent.order_completed, order);
						break;
					default:
						break;
					}
					
					
					System.out.println(order.getStatus());
				}
				else {
					ReflectionUtils.setField(field, order, value);
				}
			
		});
		
		return orderRepository.save(order);
	}
	
	private boolean statusOwner(String status,User user) throws InvalidTokenException {
		if(status.equals("COOKING") && user.getRole().equals("ROLE_cook")) {
			return true;
		}
			
		else if (status.equals("READY") && user.getRole().equals("ROLE_cook")) {
			return true;
		}
			
		else if (status.equals("SERVED") && user.getRole().equals("ROLE_waiter")) {
			return true;
		}
		else if (status.equals("PAID") && user.getRole().equals("ROLE_cashier")) {
			return true;
		}
		else if (status.equals("COMPLETED") && user.getRole().equals("ROLE_cashier")) {
			return true;
		}
		else {
			throw new InvalidTokenException("Unauthorized !");
		}
	
	}
	

	public List<TableOrder> getAllOrders() {
		
		return orderRepository.findAll();
	}


	public void deleteOrderById(Long orderId) {
		orderRepository.deleteById(orderId);
	}


	public TableOrder updateWaiter(Long orderId, Long waiterId) throws ResourceNotFoundException {
		Waiter waiter=waiterRepository.findById(waiterId).orElseThrow(()-> new ResourceNotFoundException("waiter not found"));
		TableOrder order=getOrderById(orderId);
		
		order.setWaiter(waiter);
		return orderRepository.save(order);
	}
	
	
	
}
