package com.digifood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digifood.model.Cook;

public interface CookRepository extends JpaRepository<Cook, Long>{

}
