package com.rollerspeed.rollerspeed.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rollerspeed.rollerspeed.Model.Clase;
import com.rollerspeed.rollerspeed.Service.ClaseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/clases")
@Tag(name = "Clases", description = "Gestión de las clases de patinaje")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @Operation(summary = "Listar las clases de la academia", description = "Listamos las clases de la academia, ademas las recibimos de ClaseDataConfig, donde estan alojadas de forma estatica. (Por ahora)")
    @ApiResponse(responseCode = "200", description = "Clases mostradas correctamente")
    @GetMapping
    public String listarClases(Model model) {
        List<Clase> clases = claseService.listarClases();
        model.addAttribute("clases", clases);
        model.addAttribute("hayClases", !clases.isEmpty());
        return "pages/clases/listar_clases";
    }
}
