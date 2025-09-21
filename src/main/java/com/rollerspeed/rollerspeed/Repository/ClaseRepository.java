package com.rollerspeed.rollerspeed.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rollerspeed.rollerspeed.Model.Clase;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

    Optional<Clase> findByNombreAndHorario(String nombre, String horario);

    boolean existsByNombreAndHorario(String nombre, String horario);
}
