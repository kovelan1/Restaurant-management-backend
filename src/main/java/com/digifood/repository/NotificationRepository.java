package com.digifood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digifood.model.Notification;
import com.digifood.model.User;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>{
	
	List<Notification> findByReceiver(User receiver);
}
