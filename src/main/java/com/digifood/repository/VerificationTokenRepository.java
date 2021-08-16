package com.digifood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digifood.model.User;
import com.digifood.model.VerificationToken;


public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
 
    VerificationToken findByToken(int token);
 
    VerificationToken findByUser(User user);
}

