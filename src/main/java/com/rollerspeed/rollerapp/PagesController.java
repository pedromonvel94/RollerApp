package com.rollerspeed.rollerapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Inicio | Roller Speed");
        model.addAttribute("activePage", "inicio");
        return "index";
    }

    @GetMapping("/mision")
    public String mision(Model model) {
        model.addAttribute("title", "Misión | Roller Speed");
        model.addAttribute("activePage", "mision");
        return "mision";
    }

    @GetMapping("/vision")
    public String vision(Model model) {
        model.addAttribute("title", "Visión | Roller Speed");
        model.addAttribute("activePage", "vision");
        return "vision";
    }

    @GetMapping("/valores")
    public String valores(Model model) {
        model.addAttribute("title", "Valores | Roller Speed");
        model.addAttribute("activePage", "valores");
        return "valores";
    }

    @GetMapping("/servicios")
    public String servicios(Model model) {
        model.addAttribute("title", "Servicios | Roller Speed");
        model.addAttribute("activePage", "servicios");
        return "servicios";
    }

    @GetMapping("/eventos")
    public String eventos(Model model) {
        model.addAttribute("title", "Eventos | Roller Speed");
        model.addAttribute("activePage", "eventos");
        return "eventos";
    }
}