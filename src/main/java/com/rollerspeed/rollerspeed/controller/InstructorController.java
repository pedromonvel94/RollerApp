package com.rollerspeed.rollerspeed.controller;

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

import jakarta.validation.Valid;

@Controller
@RequestMapping("/instructores")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public String listarInstructores(Model model) {
        model.addAttribute("instructores", instructorService.listarInstructores());
        return "pages/instructores/listar_instructores";
    }

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

        redirectAttributes.addFlashAttribute("mensajeExito", "Instructor registrado correctamente");
        return "redirect:/instructores";
    }
}
