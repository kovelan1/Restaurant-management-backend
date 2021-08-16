package com.digifood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digifood.model.Waiter;

@Repository
public interface WaiterRepository extends JpaRepository<Waiter, Long>{

}
