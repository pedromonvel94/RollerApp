package com.rollerspeed.rollerspeed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarioController {

    @GetMapping("/calendario/estudiantes")
    public String calendarioEstudiantes() {
        return "pages/calendario/estudiantes";
    }

    @GetMapping("/calendario/instructores")
    public String calendarioInstructores() {
        return "pages/calendario/instructores";
    }
}

