package com.rollerspeed.rollerspeed.controller;


import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller // Controlador principal
//@RequestMapping("/")
@Tag(name = "Vistas", description = "Gestión de las vistas")
public class HomeController {
    
    @Operation(summary = "Muestra el home o pagina de inicio", description = "Devuelve la página de inicio")
    @ApiResponse(responseCode = "200", description = "Página de inicio mostrada correctamente")
    @GetMapping("/")
    public String index() {
        return "pages/index"; // busca index.html en templates/pages/index.html
    }

    @Operation(summary = "Muestra la vista de la misión", description = "Devuelve la página de la misión")
    @ApiResponse(responseCode = "200", description = "Página de la misión mostrada correctamente")
    @GetMapping("/mision")
    public String mision() {
        return "pages/mision"; // busca templates/pages/mision.html
    }

    @Operation(summary = "Muestra la vista de la visión", description = "Devuelve la página de la visión")
    @ApiResponse(responseCode = "200", description = "Página de la visión mostrada correctamente")
    @GetMapping("/vision")
    public String vision() {
        return "pages/vision"; // busca templates/pages/vision.html
    }

    @Operation(summary = "Muestra la vista de los valores", description = "Devuelve la página de los valores")
    @ApiResponse(responseCode = "200", description = "Página de los valores mostrada correctamente")
    @GetMapping("/valores")
    public String valores() {
        return "pages/valores";
    }

    @Operation(summary = "Muestra la vista de los servicios", description = "Devuelve la página de los servicios")
    @ApiResponse(responseCode = "200", description = "Página de los servicios mostrada correctamente")
    @GetMapping("/servicios")
    public String servicios() {
        return "pages/servicios";
    }

    @Operation(summary = "Muestra la vista de los eventos", description = "Devuelve la página de los eventos")
    @ApiResponse(responseCode = "200", description = "Página de los eventos mostrada correctamente")
    @GetMapping("/eventos")
    public String eventos() {
        return "pages/eventos";
    }

    @Operation(summary = "Muestra la vista de los valores corporativos", description = "Devuelve la página de los valores corporativos")
    @ApiResponse(responseCode = "200", description = "Página de los valores corporativos mostrada correctamente")
    @GetMapping("/valores-corporativos")
    public String valoresCorporativos() {
        return "pages/valores_corporativos";
    }
}
