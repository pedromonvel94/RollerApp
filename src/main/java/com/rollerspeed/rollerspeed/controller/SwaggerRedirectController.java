package com.rollerspeed.rollerspeed.controller;

import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class SwaggerRedirectController {
    
    @GetMapping("/doc")
    public String redirectToSwaggerUI(Model model) {
    //     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //     String username = auth.getName(); // Nombre del usuario autenticado
    //     model.addAttribute("user",username);
       return "redirect:/swagger-ui.html"; // Redirige al path predeterminado
    }
}
