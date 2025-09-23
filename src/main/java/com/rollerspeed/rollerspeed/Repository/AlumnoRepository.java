package com.rollerspeed.rollerspeed.Repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rollerspeed.rollerspeed.Model.Alumno;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    Optional<Alumno> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}
