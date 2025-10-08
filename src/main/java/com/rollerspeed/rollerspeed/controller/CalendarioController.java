package com.rollerspeed.rollerspeed.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Calendario", description = "Gestión de calendario")
public class CalendarioController {

    @Operation(summary = "Muestra un calendario de estudiantes estatico", description = "Muestra un calendario con los eventos de los estudiantes")
    @ApiResponse(responseCode = "200", description = "Calendario mostrado correctamente")
    @GetMapping("/calendario/estudiantes")
    @PreAuthorize("hasRole('ALUMNO')")
    public String calendarioEstudiantes() {
        return "pages/calendario/estudiantes";
    }

    @Operation(summary = "Muestra un calendario de instructores estatico", description = "Muestra un calendario con los eventos de los instructores")
    @ApiResponse(responseCode = "200", description = "Calendario mostrado correctamente")
    @GetMapping("/calendario/instructores")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public String calendarioInstructores() {
        return "pages/calendario/instructores";
    }
}
