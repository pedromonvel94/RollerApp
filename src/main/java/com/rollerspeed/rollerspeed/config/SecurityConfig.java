package com.rollerspeed.rollerspeed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rollerspeed.rollerspeed.security.CustomUserDetailsService;
import com.rollerspeed.rollerspeed.security.JwtAuthenticationFilter;

/**
 * Configuración central de Spring Security.
 * Aquí definimos:
 * <ul>
 *     <li>El encoder de contraseñas (BCrypt).</li>
 *     <li>El proveedor de autenticación basado en nuestras tablas (DAO).</li>
 *     <li>Las reglas de autorización por endpoint y la inserción del filtro JWT.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Provee el encoder BCrypt que usamos al registrar alumnos e instructores.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Definimos el proveedor de autenticación que Spring Security usará para validar credenciales
     * contra nuestra implementación de {@link CustomUserDetailsService}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Exponemos el {@link AuthenticationManager} para utilizarlo desde controladores/rest,
     * p. ej. al validar credenciales en el login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Configura la cadena de filtros de seguridad:
     * - Deshabilita CSRF porque trabajaremos con JWT (stateless).
     * - Marca la sesión como STATELESS (cada request debe portar su token).
     * - Define qué rutas son públicas y cuáles requieren autenticación.
     * - Inserta nuestro filtro JWT antes del filtro de usuario/contraseña por defecto.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos para documentación y login
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/doc", "/doc/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/static/**", "/assets/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/auth/login", "/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.GET, "/alumnos/nuevo", "/instructores/nuevo").permitAll()
                        .requestMatchers(HttpMethod.POST, "/alumnos/guardar", "/instructores/guardar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        // Las vistas públicas del sitio principal
                        .requestMatchers(HttpMethod.GET,
                                "/", "/mision", "/vision", "/valores", "/servicios", "/eventos", "/valores-corporativos")
                        .permitAll()
                        // El resto de rutas requieren un JWT válido
                        .anyRequest().authenticated());

        return http.build();
    }
}
