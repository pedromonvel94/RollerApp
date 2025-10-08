package com.rollerspeed.rollerspeed.controller;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rollerspeed.rollerspeed.security.JwtService;
import com.rollerspeed.rollerspeed.security.UserPrincipal;
import com.rollerspeed.rollerspeed.security.dto.AuthRequest;
import com.rollerspeed.rollerspeed.security.dto.AuthResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Punto de entrada para autenticación con correo/contraseña.
 * Devuelve un JWT listo para usarse en el header Authorization (Bearer token).
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Emisión de JWT para el portal RollerSpeed")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Autenticación por correo y contraseña", description = "Valida las credenciales y devuelve un JWT firmado")
    @ApiResponse(responseCode = "200", description = "Inicio de sesión correcto")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword()));

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);

        String rol = principal instanceof UserPrincipal customPrincipal
                ? customPrincipal.getRol()
                : principal.getAuthorities().stream().findFirst().map(ga -> ga.getAuthority()).orElse("ROLE_USER");

        AuthResponse response = new AuthResponse(
                token,
                jwtService.getExpirationInMillis(),
                Instant.now().toString(),
                rol.replace("ROLE_", ""));

        return ResponseEntity.ok(response);
    }

    /**
     * Traducimos credenciales inválidas en una respuesta 401 clara.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
    }
}
