package com.egg.electricidad.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.electricidad.exception.MiException;
import com.egg.electricidad.model.Fabrica;
import com.egg.electricidad.service.FabricaService;

@Controller
@RequestMapping("/fabrica")
@PreAuthorize("isAuthenticated()")
public class FabricaController {

    @Autowired
    private FabricaService fabricaService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/registrar")
    public String registrar() {
        return "/fabrica/form_registro.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/registro")
    public String registrarFabrica(@RequestParam("nombreFabrica") String nombreFabrica) {

        fabricaService.guardarFabrica(nombreFabrica);

        return "redirect:/fabrica/registrar";
    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {

        List<Fabrica> fabricas = fabricaService.listarFabricas();

        modelo.addAttribute("fabricas", fabricas);

        return "/fabrica/listafabrica.html";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/modificar/{idFabrica}")
    public String modificar(@PathVariable Long idFabrica, ModelMap modelo) {
        modelo.put("fabrica", fabricaService.getOne(idFabrica));

        return "/fabrica/edit.html";
    }

    @PostMapping("/modificar/{idFabrica}")
    public String modificar(@PathVariable Long idFabrica, String nombreFabrica, ModelMap modelo) {
        try {
            fabricaService.modificarEditorial(idFabrica, nombreFabrica);

            return "redirect:../listar";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "/fabrica/edit.html";
        }

    }

}
