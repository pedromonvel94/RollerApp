package com.rollerspeed.rollerspeed.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rollerspeed.rollerspeed.Model.Instructor;
import com.rollerspeed.rollerspeed.Repository.InstructorRepository;

/**
 * Inicializa datos básicos en la base de datos para facilitar las pruebas de seguridad.
 * Crea un usuario administrador por defecto si no existe.
 */
@Configuration
public class UserDataConfig {

    @Bean
    public CommandLineRunner seedAdminUser(InstructorRepository instructorRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@rollerspeed.com";

            if (instructorRepository.findByCorreo(adminEmail).isPresent()) {
                return; // Ya existe un admin, no duplicamos registros.
            }

            Instructor admin = new Instructor();
            admin.setNombreCompleto("Administrador RollerSpeed");
            admin.setFechaNacimiento(LocalDate.of(1990, 1, 1));
            admin.setGenero("NO_BINARIO");
            admin.setTelefono("0000000000");
            admin.setCorreo(adminEmail);
            admin.setEspecialidad("Gestión General");
            admin.setNivelCertificacion("MASTER");
            admin.setAniosExperiencia(15);
            admin.setDisponibilidad("Lunes a Viernes 08:00 - 18:00");
            admin.setCertificaciones("Administrador semilla creado para pruebas de seguridad.");
            admin.setRol("ADMIN");
            admin.setPassword(passwordEncoder.encode("Admin123!"));

            instructorRepository.save(admin);
        };
    }
}
