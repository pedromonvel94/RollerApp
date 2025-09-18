package com.rollerspeed.rollerspeed.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rollerspeed.rollerspeed.Model.userModel;
import com.rollerspeed.rollerspeed.Repository.UserRepository;

@Service
public class userService {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    public List<userModel> listarUser(){
        return userRepository.findAll();
    }
    
    public void guardar(userModel usuario){
        String encodedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(encodedPassword);
        userRepository.save(usuario);
    }

    userModel buscarPorId(Long id){
        return userRepository.findById(id).orElse(null);
    }
}
