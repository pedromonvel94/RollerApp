package com.rollerspeed.rollerspeed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Se requiere tener la dependencia de Spring Security para usar PasswordEncoder.
// <dependency>
//     <groupId>org.springframework.boot</groupId>
//     <artifactId>spring-boot-starter-security</artifactId>
// </dependency>
// se codifica la contraseña para que no se guarde en texto plano en la base de datos.
// Recuerda inyectar este PasswordEncoder donde lo necesites, por ejemplo en el servicio de usuario.


@Configuration
public class passConfiguration {
    //@Bean // Expone el PasswordEncoder como un 'componente' que otros pueden usar
    //public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
    //}
     // ✅ Este bean DESACTIVA el login automático → ¡permite acceso libre a todas las rutas!
    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()  // ← ¡Todas las rutas son públicas!
            )
            .csrf(csrf -> csrf.disable()); // ← Opcional: desactiva CSRF para pruebas (solo en desarrollo)

        return http.build();
    }*/
}