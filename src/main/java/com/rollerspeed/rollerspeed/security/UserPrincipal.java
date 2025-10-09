package com.rollerspeed.rollerspeed.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.rollerspeed.rollerspeed.Model.Alumno;
import com.rollerspeed.rollerspeed.Model.Instructor;

/**
 * Implementación concreta de {@link UserDetails} que unifica el modelo de alumno e instructor.
 * Nos permite reutilizar las mismas credenciales (correo/contraseña/rol) para alimentar el contexto
 * de seguridad de Spring sin importar la tabla de origen.
 */
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final String rol;
    private final Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(Long id, String email, String password, String rol) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.rol = rol;
        // Spring Security espera un prefijo ROLE_ para construir la autoridad
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    /**
     * Crea un principal a partir de un {@link Alumno}.
     */
    public static UserPrincipal fromAlumno(Alumno alumno) {
        return new UserPrincipal(alumno.getId(), alumno.getCorreo(), alumno.getPassword(), alumno.getRol());
    }

    /**
     * Crea un principal a partir de un {@link Instructor}.
     */
    public static UserPrincipal fromInstructor(Instructor instructor) {
        return new UserPrincipal(
                instructor.getId(),
                instructor.getCorreo(),
                instructor.getPassword(),
                instructor.getRol());
    }

    public Long getId() {
        return id;
    }

    public String getRol() {
        return rol;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
