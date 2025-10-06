package com.rollerspeed.rollerspeed.config;

import java.lang.module.ModuleDescriptor.Builder;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class securityConfig {

    @Autowired //sirve para que Spring cree e inyecte automáticamente los objetos (beans) sin tener que hacer "new" manualmente
    AuthenticationConfiguration authenticationConfiguration; //Este es un objeto que ya existe en SpringSecurity y que nos permite obtener el AuthenticationManager

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{ //Este es un objeto que cada filtro le va pasando a esta clase como parametro, modificando el comportamiento del mismo.
        //Aqui en el securityFilterChain es donde se configuran las reglas o condiciones de seguridad de la aplicación web.
        return httpSecurity
            .csrf(csrf -> csrf.disable())
            .httpBasic(Customizer.withDefaults()) //Con esto activamos la autenticación básica (Basic Auth) que es un método simple para que los usuarios se autentiquen enviando su nombre de usuario y contraseña en cada solicitud HTTP.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Con esto le decimos a Spring Security que no queremos que guarde sesiones, es decir, que no guarde el estado de si un usuario está logueado o no.
            .authorizeHttpRequests(http -> {

                //Configurar los endpoints PUBLICOS
                http.requestMatchers(HttpMethod.GET, "/").permitAll(); //Con esto le decimos que la ruta raiz (/) es pública y cualquiera puede acceder sin necesidad de estar autenticado.
                http.requestMatchers(HttpMethod.GET, "/calendario/estudiantes").permitAll();//Con esto le decimos que la ruta /calendario/estudiantes es pública y cualquiera puede acceder sin necesidad de estar autenticado.
                
                //Configurar los endpoints PRIVADOS
                http.requestMatchers(HttpMethod.GET, "/alumnos/**").hasAnyAuthority("UPDATE"); //Con esto le decimos que para acceder a cualquier ruta que empiece con /alumnos/ se necesita tener el AUTHORITY de UPDATE el cual solo lo tienen INSTRUCTORES o ADMIN
                http.requestMatchers(HttpMethod.GET, "/instructores/**").hasAuthority("CREATE"); //Con esto le decimos que para acceder a cualquier ruta que empiece con /instructores/ se necesita tener el AUTHORITY de CREATE el cual solo lo tiene ADMIN
                http.requestMatchers(HttpMethod.GET, "/clases/**").hasAuthority("REGISTER"); //Con esto le decimos que para acceder a cualquier ruta que empiece con /clases/ se necesita tener el AUTHORITY de REGISTER el cual lo tienen todos los ROLES
                http.requestMatchers(HttpMethod.GET, "/swagger-ui/**").hasAuthority("CREATE"); //Con esto le decimos que la ruta /swagger-ui/ SOLO es accesible para el AUTHORITY de ADMIN
                http.requestMatchers(HttpMethod.GET, "/calendario/instructores").hasAuthority("UPDATE"); //Con esto le decimos que para acceder a cualquier ruta que empiece con /alumnos/ se necesita tener el AUTHORITY de UPDATE el cual solo lo tienen INSTRUCTORES o ADMIN

                //Configurar el resto de endpoints - NO ESPECIFICADOS
                http.anyRequest().authenticated(); //Cualquier otra ruta no especificada anteriormente requiere autenticación.
            
            })
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception{ //Este throws Exception lo coloco porque getAuthenticationManager me puede tirar una excepción
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); //Este provider en especifico es el que se encarga de autenticar a los usuarios en base a lo que se encuentre en una base de datos
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider; //En este caso UserDetailsService es el que se va a conectar directamente con la base de datos para buscar los usuarios y sus roles.
    }

    @Bean
    public UserDetailsService userDetailsService(){
        ArrayList<UserDetails> userDetails = new ArrayList<>();

        userDetails.add(User.withUsername("pedro")
                .password("1234")
                .roles("ADMIN")
                .authorities("READ", "CREATE", "UPDATE", "DELETE", "REGISTER") //Ojo, estos son los permisos que le damos a los roles y por ende a los usuarios.
                .build());
        
        userDetails.add(User.withUsername("maria")
                .password("5678")
                .roles("INSTRUCTOR")
                .authorities("READ", "UPDATE", "REGISTER")
                .build());     

        userDetails.add(User.withUsername("Luis")
                .password("8767")
                .roles("STUDENT")
                .authorities("READ", "REGISTER")
                .build());

        return new InMemoryUserDetailsManager(userDetails); //InMemoryUserDetailsManager es una implementación de UserDetailsService que almacena los detalles del usuario en memoria.
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance(); //Este es el que usamos para pruebas ya que no encripta las contraseñas
        //return new BCryptPasswordEncoder(); //Este es el que usamos para produccion ya que este en si si encripta las contraseñas
    }
}
