package com.rollerspeed.rollerspeed.controller;

import java.util.List;

//import javax.swing.Spring;

//import org.apache.catalina.User;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.rollerspeed.rollerspeed.security.dto.AuthRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/alumnos")
@PreAuthorize("denyAll()") // Bloquea el acceso a todos los métodos del controlador por defecto
@Tag(name = "Alumnos", description = "Gestión de alumnos")
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
    // Anotaciones de Swagger para documentar la API
    @Operation(summary = "Obtener una lista de todos los alumnos", description = "Devuelve una lista de todos los alumnos registrados")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
<<<<<<< HEAD
    @PreAuthorize("hasAuthority('UPDATE')") // Estoy indicando con PreAuthorize que solo INSTRUCTORES y ADMIN pueden acceder, es como lo que hice en securityConfig en el authorizeHttpRequests
=======
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
>>>>>>> origin/luis
    public String listar(Model model) {
        List<Alumno> alumnos = alumnoService.listarAlumnos();
        model.addAttribute("alumnos", alumnos);
        return "pages/alumnos/listar_alumnos";
    }

    @Operation(summary = "Obtener un formulario para crear un nuevo alumno", description = "Devuelve el formulario para crear un nuevo alumno")
    @ApiResponse(responseCode = "200", description = "Formulario obtenido correctamente")
    @GetMapping("/nuevo")
    @PreAuthorize("hasAuthority('CREATE')") // Estoy indicando con PreAuthorize que solo ADMIN puede acceder
    public String mostrarFormulario(Model model) {
        Alumno alumno = new Alumno();
        alumno.setRol("ALUMNO"); // Establece el rol predeterminado
        model.addAttribute("alumno", alumno);
        prepararOpcionesFormulario(model);
        model.addAttribute("modoEdicion", false);
        return "pages/alumnos/form_alumno";
    }

    @Operation(summary = "Sirve para guardar y crear un nuevo alumno", description = "Guarda un nuevo alumno")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Alumno creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Errores de validación en los campos del formulario"),
        @ApiResponse(responseCode = "409", description = "Correo ya registrado en el sistema")
    })
    @PostMapping("/guardar")
    @PreAuthorize("hasAuthority('CREATE')") // Estoy indicando con PreAuthorize que solo ADMIN puede crear o guardar un nuevo alumno
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

        AuthRequest authPrefill = new AuthRequest();
        authPrefill.setCorreo(alumno.getCorreo());
        redirectAttributes.addFlashAttribute("authRequest", authPrefill);
        redirectAttributes.addFlashAttribute("mensajeLogin",
                "Alumno registrado correctamente. Inicia sesión para continuar.");

        return "redirect:/auth/login";
    }

    @Operation(summary = "Obtener nuevamente el formulario para modificar un alumno existente.", description = "Devuelve el formulario para editar un alumno existente")
    @ApiResponse(responseCode = "200", description = "Formulario obtenido correctamente")
    @GetMapping("/editar/{id}")
<<<<<<< HEAD
    @PreAuthorize("hasAuthority('UPDATE')") // Estoy indicando con PreAuthorize que solo INSTRUCTORES y ADMIN pueden editar un alumno
=======
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
>>>>>>> origin/luis
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Alumno alumno = alumnoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado"));
        alumno.setPassword(PASSWORD_PLACEHOLDER);
        model.addAttribute("alumno", alumno);
        prepararOpcionesFormulario(model);
        model.addAttribute("modoEdicion", true);
        return "pages/alumnos/form_alumno";
    }

    @Operation(summary = "Actualizar un alumno existente", description = "Actualiza los datos de un alumno existente basado en el id y manejamos errores de validación y duplicados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Alumno actualizado correctamente"),
        @ApiResponse(responseCode = "409", description = "Correo ya registrado en el sistema"),
        @ApiResponse(responseCode = "302", description = "Errores de validación en los campos del formulario"),
    })
    @PostMapping("/actualizar/{id}")
<<<<<<< HEAD
    @PreAuthorize("hasAuthority('UPDATE')") // Estoy indicando con PreAuthorize que solo INSTRUCTORES y ADMIN pueden actualizar un alumno
=======
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
>>>>>>> origin/luis
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


    @Operation(summary = "Eliminar un alumno", description = "Elimina un alumno existente basado en el id")
    @ApiResponse(responseCode = "200", description = "Alumno eliminado correctamente")
    @GetMapping("/eliminar/{id}")
<<<<<<< HEAD
    @PreAuthorize("hasAuthority('DELETE')") // Estoy indicando con PreAuthorize que solo ADMIN puede eliminar un alumno
=======
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
>>>>>>> origin/luis
    public String eliminarAlumno(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        alumnoService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Alumno eliminado correctamente");
        return "redirect:/alumnos";
    }
}
