package com.rollerspeed.rollerspeed.controller;

import java.util.List;

import javax.swing.Spring;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rollerspeed.rollerspeed.Model.userModel;
import com.rollerspeed.rollerspeed.Service.userService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class userController {
    @Autowired
    userService usuarioService;


    @GetMapping("/listar")
    public String listarUser(Model model){
        List<userModel> usuarios = usuarioService.listarUser();
        model.addAttribute("usuarios", usuarios);
        return "/user/listar_user";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new userModel());
        return "/user/form_user";
    }


    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") userModel usuario, BindingResult result) {
        
        if (result.hasErrors()) {
            return "/user/form_user";
        }
        

        usuarioService.guardar(usuario);
        System.out.println("¡Usuario válido! Guardando a : " + usuario.getName());
        return "redirect:/users/listar";
    }
}
