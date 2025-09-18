package com.rollerspeed.rollerspeed.Repository;

import org.springframework.stereotype.Repository;
import com.rollerspeed.rollerspeed.Model.userModel;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<userModel, Long> {
    
}
