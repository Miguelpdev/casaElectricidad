package com.egg.electricidad.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.egg.electricidad.exception.MiException;
import com.egg.electricidad.model.Articulo;
import com.egg.electricidad.model.Fabrica;
import com.egg.electricidad.service.ArticuloService;
import com.egg.electricidad.service.FabricaService;

@Controller
@RequestMapping("/articulo")
public class ArticuloController {

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private FabricaService fabricaService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/registrar")
    public String registrar(ModelMap model) {
        List<Fabrica> fabricas = fabricaService.listarFabricas();
        model.addAttribute("fabricas", fabricas);
        return "/articulo/form_registro";
    }

    @PostMapping("/registro")
    public String registrarArticulo(
            @RequestParam("nombreArticulo") String nombreArticulo,
            @RequestParam("descripcionArticulo") String descripcionArticulo,
            @RequestParam("fabrica") Long fabricaId, MultipartFile archivo) {

        try {

            articuloService.guardarArticulo(nombreArticulo, descripcionArticulo, fabricaId, archivo);
        } catch (Exception ex) {
            Logger.getLogger(ArticuloController.class.getName()).log(Level.SEVERE, null, ex);
            return "/articulo/form_registro.html";
        }

        return "redirect:/articulo/registrar";
    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {
        List<Articulo> articulos = articuloService.listarArticulos();
        modelo.addAttribute("articulos", articulos);

        return "/articulo/listaarticulo.html";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/modificar/{idArticulo}")
    public String modificar(@PathVariable Long idArticulo, ModelMap modelo) {

        modelo.put("articulo", articuloService.getOne(idArticulo));

        List<Fabrica> fabricas = fabricaService.listarFabricas();

        modelo.addAttribute("fabricas", fabricas);

        return "/articulo/edit.html";
    }

    @PostMapping("/modificar/{idArticulo}")
    public String modificar(MultipartFile archivo, @PathVariable Long idArticulo, String nombreArticulo,
            String descripcionArticulo,
            Long idFabrica, ModelMap modelo) {
        try {
            List<Fabrica> fabricas = fabricaService.listarFabricas();

            modelo.addAttribute("fabricas", fabricas);

            articuloService.modificarArticulo(archivo, idArticulo, nombreArticulo, descripcionArticulo, idFabrica);

            return "redirect:../listar";

        } catch (MiException ex) {
            System.out.println("inggreso aqui");
            List<Fabrica> fabricas = fabricaService.listarFabricas();

            modelo.put("error", ex.getMessage());

            modelo.addAttribute("fabricas", fabricas);

            return "/articulo/edit.html";
        }

    }
}
