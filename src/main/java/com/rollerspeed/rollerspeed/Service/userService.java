package com.rollerspeed.rollerspeed.Service;

import java.util.List;

import javax.swing.text.PasswordView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rollerspeed.rollerspeed.Model.userModel;
import com.rollerspeed.rollerspeed.Repository.userRepository;

@Service
public class userService {
    @Autowired
    userRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<userModel> listarUser() {
        return userRepository.findAll();
    }

    public void guardar(userModel usuario) {
        // Antes de guardar, codigicar la contraseña
        String encodedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(encodedPassword);
        userRepository.save(usuario);
    }

    userModel buscarPorId(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
