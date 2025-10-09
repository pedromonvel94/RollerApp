package com.rollerspeed.rollerspeed.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rollerspeed.rollerspeed.Model.Alumno;
import com.rollerspeed.rollerspeed.Model.Instructor;
import com.rollerspeed.rollerspeed.Repository.AlumnoRepository;
import com.rollerspeed.rollerspeed.Repository.InstructorRepository;

/**
 * Servicio centralizado que indica a Spring Security cómo cargar un usuario por su correo electrónico.
 * Busca primero en la tabla de instructores y luego en la de alumnos para reutilizar las credenciales existentes.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final InstructorRepository instructorRepository;
    private final AlumnoRepository alumnoRepository;

    public CustomUserDetailsService(InstructorRepository instructorRepository, AlumnoRepository alumnoRepository) {
        this.instructorRepository = instructorRepository;
        this.alumnoRepository = alumnoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Intentamos autenticación por correo en ambas tablas sin duplicar lógica
        Optional<Instructor> instructorOpt = instructorRepository.findByCorreo(username);
        if (instructorOpt.isPresent()) {
            return UserPrincipal.fromInstructor(instructorOpt.get());
        }

        Optional<Alumno> alumnoOpt = alumnoRepository.findByCorreo(username);
        if (alumnoOpt.isPresent()) {
            return UserPrincipal.fromAlumno(alumnoOpt.get());
        }

        throw new UsernameNotFoundException("No se encontró un usuario registrado con el correo: " + username);
    }
}
