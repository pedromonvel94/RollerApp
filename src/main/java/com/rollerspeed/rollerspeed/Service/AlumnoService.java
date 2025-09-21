package com.rollerspeed.rollerspeed.Service;

import java.util.List;
import java.util.Optional;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rollerspeed.rollerspeed.Model.Alumno;
import com.rollerspeed.rollerspeed.Repository.AlumnoRepository;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final PasswordEncoder passwordEncoder;

    public AlumnoService(AlumnoRepository alumnoRepository, PasswordEncoder passwordEncoder) {
        this.alumnoRepository = alumnoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Alumno> listarAlumnos() {
        return alumnoRepository.findAll();
    }

    public Optional<Alumno> buscarPorId(Long id) {
        return alumnoRepository.findById(id);
    }

    public boolean existeCorreo(String correo) {
        return alumnoRepository.existsByCorreo(correo);
    }

    public Alumno guardar(Alumno alumno) {
        if (alumnoRepository.existsByCorreo(alumno.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        alumno.setPassword(passwordEncoder.encode(alumno.getPassword()));
        alumno.setRol("ALUMNO");
        if (alumno.getEstadoPago() == null || alumno.getEstadoPago().isBlank()) {
            alumno.setEstadoPago("Pendiente");
        }
        return alumnoRepository.save(alumno);
    }

    public Alumno actualizar(Alumno alumno) {
        Alumno existente = alumnoRepository.findById(alumno.getId())
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        if (!alumno.getCorreo().equals(existente.getCorreo()) && alumnoRepository.existsByCorreo(alumno.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        existente.setNombreCompleto(alumno.getNombreCompleto());
        existente.setFechaNacimiento(alumno.getFechaNacimiento());
        existente.setGenero(alumno.getGenero());
        existente.setCorreo(alumno.getCorreo());
        existente.setTelefono(alumno.getTelefono());
        existente.setMedioPago(alumno.getMedioPago());
        existente.setEstadoPago(alumno.getEstadoPago());
        existente.setRol("ALUMNO");

        if (alumno.getPassword() != null && !alumno.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(alumno.getPassword()));
        }

        return alumnoRepository.save(existente);
    }

    public void eliminar(Long id) {
        alumnoRepository.deleteById(id);
    }
}
