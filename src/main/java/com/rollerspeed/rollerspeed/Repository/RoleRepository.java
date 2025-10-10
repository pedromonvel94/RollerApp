package com.rollerspeed.rollerspeed.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rollerspeed.rollerspeed.Model.RolesEntity;

@Repository
public interface RoleRepository extends JpaRepository<RolesEntity, Long> {
    Optional<RolesEntity> findByName(String name); // "ALUMNO", "INSTRUCTOR", etc.
}