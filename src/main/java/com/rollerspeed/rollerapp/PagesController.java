package com.rollerspeed.rollerapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

    @GetMapping("/")
    public String index() {
        return "index"; // buscará templates/index.html
    }

    @GetMapping("/mision")
    public String mision() {
        return "mision"; // buscará templates/mision.html
    }
}