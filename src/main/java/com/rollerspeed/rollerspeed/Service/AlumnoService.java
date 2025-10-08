package com.rollerspeed.rollerspeed.Service;

import java.util.List;
import java.util.Optional;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rollerspeed.rollerspeed.Model.Alumno;
import com.rollerspeed.rollerspeed.Model.RoleEntity;
import com.rollerspeed.rollerspeed.Model.UserAccount;
import com.rollerspeed.rollerspeed.Repository.AlumnoRepository;
import com.rollerspeed.rollerspeed.Repository.RoleRepository;
import com.rollerspeed.rollerspeed.Repository.UserAccountRepository;

import jakarta.transaction.Transactional;

@Service
public class AlumnoService {

    private static final String ROLE_ALUMNO = "ALUMNO";
    private static final String PASSWORD_PLACEHOLDER = "********";

    private final AlumnoRepository alumnoRepository;
    private final UserAccountRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    public AlumnoService(AlumnoRepository alumnoRepository, UserAccountRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        this.alumnoRepository = alumnoRepository;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
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

    @Transactional
    public Alumno guardar(Alumno alumno) {
        // Validaciones de correo
        if (alumnoRepository.existsByCorreo(alumno.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        if (userRepo.existsByUsername(alumno.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está asignado como usuario");
        }

        // Defaults de dominio
        alumno.setRol(ROLE_ALUMNO);
        if (alumno.getEstadoPago() == null || alumno.getEstadoPago().isBlank()) {
            alumno.setEstadoPago("Pendiente");
        }

        // Persistimos primero el Alumno
        Alumno saved = alumnoRepository.save(alumno);

        // Creamos la cuenta de usuario con el mismo correo
        RoleEntity rolAlumno = roleRepo.findByName(ROLE_ALUMNO)
                .orElseThrow(() -> new IllegalStateException("Falta rol " + ROLE_ALUMNO));

        String rawPassword = alumno.getPassword();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        UserAccount user = UserAccount.builder()
                .username(saved.getCorreo()) // email como username
                .password(passwordEncoder.encode(rawPassword))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .alumno(saved)
                .build();
        user.getRoles().add(rolAlumno);

        userRepo.save(user);

        // (Opcional) Limpia el password en el objeto en memoria
        saved.setPassword(PASSWORD_PLACEHOLDER);

        return saved;
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
