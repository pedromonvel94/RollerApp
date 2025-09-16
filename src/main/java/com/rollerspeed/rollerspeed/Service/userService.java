package com.rollerspeed.rollerspeed.Servide;

import java.util.List;

import javax.swing.text.PasswordView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rollerspeed.rollerspeed.Model.userModel;
import com.rollerspeed.rollerspeed.Repository.UserRepository;

@Service
public class userService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordView passwordEncoder;

    public List<userModel> listarUser() {
        return userRepository.findAll();
    }

    public void guardar(userModel usurio){
        //Antes de guardar, codigicar la contraseña
        String encodedPassword = passwordEncoder.encode(usurio.getPassword());
        usurio.setPassword(encodedPassword);
    }
}
