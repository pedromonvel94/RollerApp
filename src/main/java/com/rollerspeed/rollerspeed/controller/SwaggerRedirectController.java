package com.rollerspeed.rollerspeed.controller;

import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@Tag(name = "Swagger UI", description = "Gestión de la documentación de la API")
public class SwaggerRedirectController {
    
    @Operation(summary = "Redirecciona al UI de la API con swagger", description = "Muestra la vista de la documentación de la API")
    @ApiResponse(responseCode = "200", description = "Documentación mostrada correctamente")
    @GetMapping("/doc")
    public String redirectToSwaggerUI(Model model) {
    //     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //     String username = auth.getName(); // Nombre del usuario autenticado
    //     model.addAttribute("user",username);
       return "redirect:/swagger-ui.html"; // Redirige al path predeterminado
    }
}
