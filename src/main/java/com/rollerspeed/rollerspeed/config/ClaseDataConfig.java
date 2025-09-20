package com.rollerspeed.rollerspeed.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rollerspeed.rollerspeed.Model.Clase;
import com.rollerspeed.rollerspeed.Repository.ClaseRepository;

@Configuration
public class ClaseDataConfig {

    @Bean
    public CommandLineRunner initClases(ClaseRepository claseRepository) {
        return args -> {
            if (claseRepository.count() > 0) {
                return;
            }

            List<Clase> clases = List.of(
                    new Clase(null,
                            "Funcional Básico",
                            "Sesión enfocada en desarrollar fuerza y equilibrio para quienes están iniciando en el patinaje.",
                            "BÁSICO",
                            "Lunes y miércoles",
                            "06:00 - 07:00",
                            "Sede Norte - Pista cubierta",
                            20,
                            60,
                            "ACTIVA"),
                    new Clase(null,
                            "Funcional Power",
                            "Entrenamiento de alta intensidad para potenciar potencia y resistencia en patinadores intermedios.",
                            "INTERMEDIO",
                            "Martes y jueves",
                            "18:30 - 20:00",
                            "Sede Central - Gimnasio funcional",
                            18,
                            90,
                            "ACTIVA"),
                    new Clase(null,
                            "Funcional Elite",
                            "Programa orientado a patinadores avanzados con foco en velocidad, coordinación y explosividad.",
                            "AVANZADO",
                            "Sábados",
                            "07:00 - 09:00",
                            "Sede Norte - Circuito urbano",
                            16,
                            120,
                            "ACTIVA"));

            claseRepository.saveAll(clases);
        };
    }
}
