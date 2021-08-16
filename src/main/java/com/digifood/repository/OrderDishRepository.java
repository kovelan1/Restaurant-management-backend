package com.digifood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digifood.model.OrderDish;
import com.digifood.model.OrderDishKey;

public interface OrderDishRepository extends JpaRepository<OrderDish, OrderDishKey>{

}
