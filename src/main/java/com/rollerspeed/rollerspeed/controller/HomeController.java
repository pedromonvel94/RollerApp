package com.rollerspeed.rollerspeed.controller;


import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Controlador principal
//@RequestMapping("/")
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "pages/index"; // busca index.html en templates/pages/index.html
    }

    @GetMapping("/mision")
    public String mision() {
        return "pages/mision"; // busca templates/pages/mision.html
    }

    @GetMapping("/vision")
    public String vision() {
        return "pages/vision"; // busca templates/pages/vision.html
    }

    @GetMapping("/valores")
    public String valores() {
        return "pages/valores";
    }

    @GetMapping("/servicios")
    public String servicios() {
        return "pages/servicios";
    }

    @GetMapping("/eventos")
    public String eventos() {
        return "pages/eventos";
    }

    @GetMapping("/valores-corporativos")
    public String valoresCorporativos() {
        return "pages/valores_corporativos";
    }
}
