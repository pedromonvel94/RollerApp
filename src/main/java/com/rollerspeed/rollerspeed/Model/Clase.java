package com.rollerspeed.rollerspeed.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_clases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la clase es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar 120 caracteres")
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotBlank(message = "El nivel es obligatorio")
    @Size(max = 30, message = "El nivel no puede superar 30 caracteres")
    @Column(nullable = false, length = 30)
    private String nivel;

    @NotBlank(message = "Debe especificar los días")
    @Size(max = 60, message = "Los días no pueden superar 60 caracteres")
    @Column(nullable = false, length = 60)
    private String dias;

    @NotBlank(message = "Debe indicar el horario")
    @Size(max = 60, message = "El horario no puede superar 60 caracteres")
    @Column(nullable = false, length = 60)
    private String horario;

    @NotBlank(message = "Debe indicar la ubicación")
    @Size(max = 80, message = "La ubicación no puede superar 80 caracteres")
    @Column(nullable = false, length = 80)
    private String ubicacion;

    @NotNull(message = "El cupo máximo es obligatorio")
    @Min(value = 1, message = "El cupo mínimo es 1")
    @Column(nullable = false)
    private Integer cupoMaximo;

    @NotNull(message = "Debe indicar la duración en minutos")
    @Min(value = 15, message = "La duración mínima es 15 minutos")
    @Column(nullable = false)
    private Integer duracionMinutos;

    @NotBlank(message = "Debe indicar el estado")
    @Size(max = 30, message = "El estado no puede superar 30 caracteres")
    @Column(nullable = false, length = 30)
    private String estado;
}
