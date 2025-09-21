package com.rollerspeed.rollerspeed.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rollerspeed.rollerspeed.Model.Instructor;
import com.rollerspeed.rollerspeed.Repository.InstructorRepository;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;

    public InstructorService(InstructorRepository instructorRepository, PasswordEncoder passwordEncoder) {
        this.instructorRepository = instructorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Instructor> listarInstructores() {
        return instructorRepository.findAll();
    }

    public Optional<Instructor> buscarPorId(Long id) {
        return instructorRepository.findById(id);
    }

    public boolean existeCorreo(String correo) {
        return instructorRepository.existsByCorreo(correo);
    }

    public Instructor guardar(Instructor instructor) {
        if (instructorRepository.existsByCorreo(instructor.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        instructor.setPassword(passwordEncoder.encode(instructor.getPassword()));
        instructor.setRol("INSTRUCTOR");
        return instructorRepository.save(instructor);
    }

    public Instructor actualizar(Instructor instructor) {
        Instructor existente = instructorRepository.findById(instructor.getId())
                .orElseThrow(() -> new IllegalArgumentException("Instructor no encontrado"));

        if (!instructor.getCorreo().equals(existente.getCorreo())
                && instructorRepository.existsByCorreo(instructor.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        existente.setNombreCompleto(instructor.getNombreCompleto());
        existente.setFechaNacimiento(instructor.getFechaNacimiento());
        existente.setGenero(instructor.getGenero());
        existente.setTelefono(instructor.getTelefono());
        existente.setCorreo(instructor.getCorreo());
        existente.setEspecialidad(instructor.getEspecialidad());
        existente.setNivelCertificacion(instructor.getNivelCertificacion());
        existente.setAniosExperiencia(instructor.getAniosExperiencia());
        existente.setDisponibilidad(instructor.getDisponibilidad());
        existente.setCertificaciones(instructor.getCertificaciones());
        existente.setRol("INSTRUCTOR");

        if (instructor.getPassword() != null && !instructor.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(instructor.getPassword()));
        }

        return instructorRepository.save(existente);
    }

    public void eliminar(Long id) {
        instructorRepository.deleteById(id);
    }
}
