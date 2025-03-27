package com.egg.electricidad.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.electricidad.exception.MiException;
import com.egg.electricidad.model.Fabrica;
import com.egg.electricidad.repository.FabricaRepository;

@Service
public class FabricaService {

    @Autowired
    private FabricaRepository fabricaRepository;

    @Transactional
    public Fabrica guardarFabrica(String nombreFabrica) {
        Fabrica nuevaFabrica = new Fabrica();
        nuevaFabrica.setNombreFabrica(nombreFabrica);
        return fabricaRepository.save(nuevaFabrica);
    }

    // Método para encontrar una fábrica por ID
    public Optional<Fabrica> findById(Long idFabrica) {
        return fabricaRepository.findById(idFabrica);
    }

    @Transactional(readOnly = true)
    public List<Fabrica> listarFabricas() {
        List<Fabrica> fabricas = new ArrayList<>();
        fabricas = fabricaRepository.findAll();
        return fabricas;
    }

    @Transactional(readOnly = true)
    public Fabrica getOne(Long idFabrica) {
        return fabricaRepository.getReferenceById(idFabrica);
    }

    @Transactional
    public void modificarEditorial(Long id, String nombre) throws MiException {
        validar(nombre);
        Optional<Fabrica> respuesta = fabricaRepository.findById(id);
        if (respuesta.isPresent()) {
            Fabrica fabrica = respuesta.get();
            fabrica.setNombreFabrica(nombre);
            fabricaRepository.save(fabrica);
        }
    }

    public void validar(String nombre) throws MiException {
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre de la fabrica no puede estar vacío o ser nulo");
        }
    }

}
