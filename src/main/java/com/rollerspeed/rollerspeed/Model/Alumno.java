package com.rollerspeed.rollerspeed.Model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@Entity // ¡Esto le dice a JPA que es una tabla!
@Data
@NoArgsConstructor
@AllArgsConstructor // Nombre de la tabla en la BD
@Table(name = "tbl_alumnos")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo", nullable = false, length = 120)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombreCompleto;

    @Column(name = "fecha_nacimiento", nullable = false)
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe estar en el pasado")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "El género es obligatorio")
    private String genero;

    @Column(nullable = false, length = 60, unique = true)
    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @Column(name = "medio_pago", nullable = false, length = 30)
    @NotBlank(message = "Debe seleccionar un medio de pago")
    private String medioPago;

    @Column(name = "estado_pago", nullable = false, length = 20)
    @NotBlank(message = "El estado de pago es obligatorio")
    private String estadoPago;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Debe asignarse un rol")
    private String rol;

    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

}