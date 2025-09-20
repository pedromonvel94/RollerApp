package com.rollerspeed.rollerspeed.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rollerspeed.rollerspeed.Model.Clase;
import com.rollerspeed.rollerspeed.Repository.ClaseRepository;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;

    public ClaseService(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    public List<Clase> listarClases() {
        return claseRepository.findAll();
    }

    public Optional<Clase> buscarPorId(Long id) {
        return claseRepository.findById(id);
    }

    public Clase guardar(Clase clase) {
        if (claseRepository.existsByNombreAndHorario(clase.getNombre(), clase.getHorario())) {
            throw new IllegalArgumentException("Ya existe una clase con el mismo nombre y horario");
        }
        clase.setEstado(clase.getEstado().toUpperCase());
        return claseRepository.save(clase);
    }

    public Clase actualizar(Clase clase) {
        Clase existente = claseRepository.findById(clase.getId())
                .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada"));

        boolean cambioNombreHorario = !existente.getNombre().equals(clase.getNombre())
                || !existente.getHorario().equals(clase.getHorario());

        if (cambioNombreHorario
                && claseRepository.existsByNombreAndHorario(clase.getNombre(), clase.getHorario())) {
            throw new IllegalArgumentException("Ya existe una clase con el mismo nombre y horario");
        }

        existente.setNombre(clase.getNombre());
        existente.setDescripcion(clase.getDescripcion());
        existente.setNivel(clase.getNivel());
        existente.setDias(clase.getDias());
        existente.setHorario(clase.getHorario());
        existente.setUbicacion(clase.getUbicacion());
        existente.setCupoMaximo(clase.getCupoMaximo());
        existente.setDuracionMinutos(clase.getDuracionMinutos());
        existente.setEstado(clase.getEstado().toUpperCase());

        return claseRepository.save(existente);
    }

    public void eliminar(Long id) {
        claseRepository.deleteById(id);
    }
}
