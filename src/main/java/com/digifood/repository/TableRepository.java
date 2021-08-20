package com.digifood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digifood.model.RestaurantTable;

public interface TableRepository extends JpaRepository<RestaurantTable, Long>{

}
