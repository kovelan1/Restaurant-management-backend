package com.digifood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digifood.model.TableOrder;

public interface OrderRepository extends JpaRepository<TableOrder, Long>{

}
