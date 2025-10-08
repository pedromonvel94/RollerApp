package com.rollerspeed.rollerspeed.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rollerspeed.rollerspeed.security.JwtAuthenticationFilter;
import com.rollerspeed.rollerspeed.security.JwtService;
import com.rollerspeed.rollerspeed.security.UserPrincipal;
import com.rollerspeed.rollerspeed.security.dto.AuthRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * Controlador MVC para flujo de login/logout con vistas Thymeleaf.
 */
@Controller
@RequestMapping("/auth")
@Tag(name = "Vistas de autenticación", description = "Controla el login/logout con páginas Thymeleaf")
public class AuthViewController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthViewController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Muestra el formulario de inicio de sesión")
    @GetMapping("/login")
    public String loginForm(Model model) {
        if (!model.containsAttribute("authRequest")) {
            model.addAttribute("authRequest", new AuthRequest());
        }
        return "pages/auth/login";
    }

    @Operation(summary = "Procesa el inicio de sesión y redirige según el rol")
    @PostMapping("/login")
    public String loginSubmit(@Valid @ModelAttribute("authRequest") AuthRequest request,
            BindingResult bindingResult,
            HttpServletResponse response,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "pages/auth/login";
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails principal = (UserDetails) authentication.getPrincipal();

            String token = jwtService.generateToken(principal);
            ResponseCookie cookie = ResponseCookie.from(JwtAuthenticationFilter.JWT_COOKIE_NAME, token)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(Duration.ofMillis(jwtService.getExpirationInMillis()))
                    .sameSite("Lax")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            String rol = principal instanceof UserPrincipal userPrincipal
                    ? userPrincipal.getRol()
                    : principal.getAuthorities().stream()
                            .findFirst()
                            .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                            .orElse("ALUMNO");

            redirectAttributes.addFlashAttribute("mensajeExito", "Bienvenido de nuevo, " + request.getCorreo());

            return switch (rol) {
                case "ADMIN" -> "redirect:/instructores";
                case "INSTRUCTOR" -> "redirect:/alumnos";
                default -> "redirect:/clases";
            };
        } catch (BadCredentialsException ex) {
            bindingResult.reject("auth.invalid", "Correo o contraseña incorrectos");
            model.addAttribute("authError", "Correo o contraseña incorrectos");
            return "pages/auth/login";
        }
    }

    @Operation(summary = "Cierra sesión desde la vista y elimina el JWT")
    @PostMapping("/logout")
    public String logout(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        SecurityContextHolder.clearContext();
        ResponseCookie cookie = ResponseCookie.from(JwtAuthenticationFilter.JWT_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        redirectAttributes.addFlashAttribute("mensajeExito", "Sesión cerrada correctamente.");
        return "redirect:/";
    }
}
