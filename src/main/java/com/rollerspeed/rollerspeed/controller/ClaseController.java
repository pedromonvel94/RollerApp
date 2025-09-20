package com.rollerspeed.rollerspeed.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rollerspeed.rollerspeed.Model.Clase;
import com.rollerspeed.rollerspeed.Service.ClaseService;

@Controller
@RequestMapping("/clases")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @GetMapping
    public String listarClases(Model model) {
        List<Clase> clases = claseService.listarClases();
        model.addAttribute("clases", clases);
        model.addAttribute("hayClases", !clases.isEmpty());
        return "pages/clases/listar_clases";
    }
}
