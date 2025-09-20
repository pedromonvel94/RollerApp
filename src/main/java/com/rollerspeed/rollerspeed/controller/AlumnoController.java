package com.rollerspeed.rollerspeed.controller;

import java.util.List;

//import javax.swing.Spring;

//import org.apache.catalina.User;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rollerspeed.rollerspeed.Model.Alumno;
import com.rollerspeed.rollerspeed.Service.AlumnoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/alumnos")
public class AlumnoController {

    private static final String PASSWORD_PLACEHOLDER = "********";

    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    private void prepararOpcionesFormulario(Model model) {
        model.addAttribute("estadosPago", List.of("Pendiente", "Pago", "Mora"));
        model.addAttribute("mediosPago", List.of("Tarjeta de Crédito", "Transferencia Bancaria", "Efectivo"));
    }

    @GetMapping
    public String listar(Model model) {
        List<Alumno> alumnos = alumnoService.listarAlumnos();
        model.addAttribute("alumnos", alumnos);
        return "pages/alumnos/listar_alumnos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        Alumno alumno = new Alumno();
        alumno.setRol("ALUMNO"); // Establece el rol predeterminado
        model.addAttribute("alumno", alumno);
        prepararOpcionesFormulario(model);
        model.addAttribute("modoEdicion", false);
        return "pages/alumnos/form_alumno";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("alumno") Alumno alumno, BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {

        if (alumnoService.existeCorreo(alumno.getCorreo())) {
            result.rejectValue("correo", "correo.duplicado", "El correo ya está registrado");
        }

        if (result.hasErrors()) {
            prepararOpcionesFormulario(model);
            model.addAttribute("modoEdicion", false);
            return "pages/alumnos/form_alumno";
        }

        alumnoService.guardar(alumno);
        redirectAttributes.addFlashAttribute("mensajeExito", "Alumno registrado correctamente");
        return "redirect:/alumnos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Alumno alumno = alumnoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado"));
        alumno.setPassword(PASSWORD_PLACEHOLDER);
        model.addAttribute("alumno", alumno);
        prepararOpcionesFormulario(model);
        model.addAttribute("modoEdicion", true);
        return "pages/alumnos/form_alumno";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarAlumno(@PathVariable Long id, @Valid @ModelAttribute("alumno") Alumno alumno,
            BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        alumno.setId(id);
        boolean mantenerPassword = PASSWORD_PLACEHOLDER.equals(alumno.getPassword());
        if (mantenerPassword) {
            alumno.setPassword(null);
        }

        if (result.hasErrors()) {
            if (mantenerPassword || alumno.getPassword() == null) {
                alumno.setPassword(PASSWORD_PLACEHOLDER);
            }
            prepararOpcionesFormulario(model);
            model.addAttribute("modoEdicion", true);
            return "pages/alumnos/form_alumno";
        }

        try {
            alumnoService.actualizar(alumno);
        } catch (IllegalArgumentException e) {
            result.rejectValue("correo", "correo.duplicado", e.getMessage());
            alumno.setPassword(PASSWORD_PLACEHOLDER);
            prepararOpcionesFormulario(model);
            model.addAttribute("modoEdicion", true);
            return "pages/alumnos/form_alumno";
        }

        redirectAttributes.addFlashAttribute("mensajeExito", "Alumno actualizado correctamente");
        return "redirect:/alumnos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAlumno(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        alumnoService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Alumno eliminado correctamente");
        return "redirect:/alumnos";
    }
}
