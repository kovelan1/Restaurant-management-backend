package com.digifood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digifood.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}
