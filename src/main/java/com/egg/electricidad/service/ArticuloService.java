package com.egg.electricidad.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.egg.electricidad.exception.MiException;
import com.egg.electricidad.model.Articulo;
import com.egg.electricidad.model.Fabrica;
import com.egg.electricidad.model.Imagen;
import com.egg.electricidad.repository.ArticuloRepository;

@Service
public class ArticuloService {

    // private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private FabricaService fabricaService;

    @Autowired
    private ImagenService imagenService;

    @Transactional
    public Articulo guardarArticulo(String nombreArticulo, String descripcionArticulo, Long fabricaId,
            MultipartFile archivo)
            throws MiException {

        validar(nombreArticulo, descripcionArticulo, fabricaId);

        Integer lastNroArticulo = articuloRepository.findMaxNroArticulo();
        int nextNroArticulo = (lastNroArticulo == null) ? 1 : lastNroArticulo + 1;

        Articulo articulo = new Articulo();

        articulo.setNroArticulo(nextNroArticulo);
        articulo.setNombreArticulo(nombreArticulo);
        articulo.setDescripcionArticulo(descripcionArticulo);

        Fabrica fabrica = fabricaService.findById(fabricaId)
                .orElseThrow(() -> new RuntimeException("Fábrica no encontrada"));

        articulo.setFabrica(fabrica);

        if (archivo != null && !archivo.isEmpty()) { // Si el archivo existe y no está vacío
            Imagen imagen = imagenService.guardar(archivo); // guardo la imagen del usuario
            articulo.setImagen(imagen); // seteo la imagen al usuario
        }

        return articuloRepository.save(articulo);
    }

    @Transactional
    public void modificarArticulo(MultipartFile archivo, Long idArticulo, String nombreArticulo,
            String descripcionArticulo, Long idFabrica)
            throws MiException {
        validar(nombreArticulo, descripcionArticulo, idFabrica);
        Optional<Articulo> respuesta = articuloRepository.findById(idArticulo);
        Optional<Fabrica> respuestaFabrica = fabricaService.findById(idFabrica);

        Fabrica fabrica = new Fabrica();

        if (respuestaFabrica.isPresent()) {
            fabrica = respuestaFabrica.get();
        }

        if (respuesta.isPresent()) {
            Articulo articulo = respuesta.get();
            articulo.setNombreArticulo(nombreArticulo);
            articulo.setDescripcionArticulo(descripcionArticulo);
            if (archivo != null && !archivo.isEmpty()) { // Si el archivo existe y no está vacío
                Imagen imagen = imagenService.guardar(archivo); // guardo la imagen del usuari
                articulo.setImagen(imagen); // seteo la imagen al usuario
            }
            articulo.setFabrica(fabrica);
        }
    }

    @Transactional(readOnly = true)
    public List<Articulo> listarArticulos() {

        List<Articulo> articulos = new ArrayList<>();

        articulos = articuloRepository.findAll();
        return articulos;
    }

    @Transactional(readOnly = true)
    public Articulo getOne(Long idArticulo) {
        return articuloRepository.getReferenceById(idArticulo);
    }

    private void validar(String nombreArticulo, String descripcionArticulo, Long fabricaId)
            throws MiException {

        if (nombreArticulo == null) {
            throw new MiException("el isbn no puede ser nulo");
        }
        if (descripcionArticulo.isEmpty() || descripcionArticulo == null) {
            throw new MiException("el titulo no puede ser nulo o estar vacio");
        }
        if (fabricaId == null) {
            throw new MiException("el Autor no puede ser nulo o estar vacio");
        }
    }

}
