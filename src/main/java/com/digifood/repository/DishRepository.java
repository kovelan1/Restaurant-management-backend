package com.digifood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digifood.model.Dish;

public interface DishRepository extends JpaRepository<Dish, Long>{

}
