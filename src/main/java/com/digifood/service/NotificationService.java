package com.digifood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digifood.model.Notification;
import com.digifood.model.NotificationEvent;
import com.digifood.model.TableOrder;
import com.digifood.model.User;
import com.digifood.repository.NotificationRepository;

@Service
public class NotificationService {
	
	@Autowired 
	private NotificationRepository notifyRepo;
	
	@Autowired
	private UserService userService;
	

	public void createNotification(User receiver, User actionBy, NotificationEvent event, TableOrder order) {
		Notification notification=new Notification(receiver, actionBy, event,order);
		notifyRepo.save(notification);
	}

	public List<Notification> getByReceiver(String name) {
		User user=userService.getUserByUsername(name);
		
		return notifyRepo.findByReceiver(user);
	}
}
