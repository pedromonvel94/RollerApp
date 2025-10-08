package com.rollerspeed.rollerspeed.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rollerspeed.rollerspeed.Model.Instructor;
import com.rollerspeed.rollerspeed.Service.InstructorService;
import com.rollerspeed.rollerspeed.security.dto.AuthRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/instructores")
@Tag(name = "Instructores", description = "Gestión de instructores")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @Operation(summary = "Obtener una lista de todos los instructores", description = "Devuelve una lista de todos los instructores registrados")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    public String listarInstructores(Model model) {
        model.addAttribute("instructores", instructorService.listarInstructores());
        return "pages/instructores/listar_instructores";
    }

    @Operation(summary = "Obtener un formulario para crear un nuevo instructor", description = "Devuelve el formulario para crear un nuevo instructor")
    @ApiResponse(responseCode = "200", description = "Formulario obtenido correctamente")
    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        if (!model.containsAttribute("instructor")) {
            Instructor instructor = new Instructor();
            instructor.setRol("INSTRUCTOR");
            model.addAttribute("instructor", instructor);
        }
        model.addAttribute("modoEdicion", false);
        return "pages/instructores/form_instructor";
    }

    @Operation(summary = "Sirve para guardar y crear un nuevo instructor", description = "Guarda un nuevo instructor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Instructor creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Errores de validación en los campos del formulario"),
        @ApiResponse(responseCode = "409", description = "Correo ya registrado en el sistema")
    })
    @PostMapping("/guardar")
    public String guardarInstructor(@Valid @ModelAttribute("instructor") Instructor instructor, BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            instructor.setRol("INSTRUCTOR");
            model.addAttribute("modoEdicion", false);
            return "pages/instructores/form_instructor";
        }

        try {
            instructorService.guardar(instructor);
        } catch (IllegalArgumentException ex) {
            result.rejectValue("correo", "correo.duplicado", ex.getMessage());
            instructor.setRol("INSTRUCTOR");
            model.addAttribute("modoEdicion", false);
            return "pages/instructores/form_instructor";
        }

        AuthRequest authPrefill = new AuthRequest();
        authPrefill.setCorreo(instructor.getCorreo());
        redirectAttributes.addFlashAttribute("authRequest", authPrefill);
        redirectAttributes.addFlashAttribute("mensajeLogin",
                "Instructor registrado correctamente. Inicia sesión para acceder a tus funciones.");
        return "redirect:/auth/login";
    }
}
