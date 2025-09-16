package com.rollerspeed.rollerspeed.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity // ¡Esto le dice a JPA que es una tabla!
@Table(name = "tbl_users") // Nombre de la tabla en la BD
@Data
@NoArgsConstructor
@AllArgsConstructor
public class userModel {
    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    Long id;
    
    @Column(name = "nombre", nullable = true, length = 50) // Nombre de la columna en la BD
    @NotBlank(message = "El nombre es obligatorio")
    String name;    
    
    @Column(name = "e_mail", nullable = false, length = 40)
    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es obligatorio")
    String email;
    

    @NotBlank(message = "La contraseña es obligatoria 🔐")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String password;
}